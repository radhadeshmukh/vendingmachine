package com.test.vending;

import com.test.vending.SimpleVendingMachine;
import com.test.vending.vo.Coin;
import com.test.vending.vo.Item;
import com.test.vending.vo.Slot;
import com.test.vending.vo.State;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SimpleVendingMachineTest {

    @Test
    public void testLoadSlot() {
        int priceInCents = 25;
        SimpleVendingMachine vm = new SimpleVendingMachine();
        assertThat(vm.getCurrentState(), is(equalTo(State.READY)));
        vm.loadSlot(1, 1, priceInCents, Item.Coke);
        assertThat(vm.getCurrentState(), is(equalTo(State.READY)));

        Map<Integer, Slot> slots = Whitebox.getInternalState(vm, "slots");
        assertThat(slots.get(1).getItem(), is(equalTo(Item.Coke)));
        assertThat(slots.get(1).getQuantity(), is(equalTo(1)));
    }

    @Test
    public void testInsertCoin() {
        Coin insertedCoin = new Coin(25);
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.insertCoin(insertedCoin);
        assertThat(vm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
        List<Coin> coins = Whitebox.getInternalState(vm, "userCoins");

        assertThat(coins.size(), is(equalTo(1)));
        assertThat(coins.get(0), is(equalTo(insertedCoin)));
    }

    @Test
    public void testSelectItemAndReturnVendingMachineChange() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.initialize();

        vm.insertCoin(new Coin(25));
        vm.insertCoin(new Coin(25));
        Pair<List<Coin>, Item> changeItemPair = vm.selectItem(3);
        List<Coin> coins = changeItemPair.getLeft();
        Item item = changeItemPair.getRight();
        assertThat(item, is(equalTo(Item.Soda)));
        assertThat(coins.size(), is(equalTo(1)));
        assertThat(coins.get(0).getValue(), is(equalTo(5)));
    }

    @Test
    public void testSelectItemAndReturnBackChange() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.loadSlot(1, 1, 25, Item.Coke);

        vm.insertCoin(new Coin(25));
        vm.insertCoin(new Coin(25));
        Pair<List<Coin>, Item> changeItemPair = vm.selectItem(1);
        List<Coin> coins = changeItemPair.getLeft();
        Item item = changeItemPair.getRight();
        assertThat(item, is(equalTo(Item.Coke)));
        assertThat(coins.size(), is(equalTo(1)));
        assertThat(coins.get(0).getValue(), is(equalTo(25)));
    }

    @Test
    public void testSelectItemAndNoChange() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.loadSlot(1, 1, 25, Item.Coke);

        vm.insertCoin(new Coin(25));
        Pair<List<Coin>, Item> changeItemPair = vm.selectItem(1);
        List<Coin> coins = changeItemPair.getLeft();
        Item item = changeItemPair.getRight();
        assertThat(item, is(equalTo(Item.Coke)));
        assertThat(coins.size(), is(equalTo(0)));
    }

    @Test
    public void testSelectItemOutOfStock() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.loadSlot(2, 0, 35, Item.Pepsi);
        vm.insertCoin(new Coin(25));
        vm.insertCoin(new Coin(10));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Pair<List<Coin>, Item> changeItemPair = vm.selectItem(2);

        assertThat(changeItemPair, is(nullValue()));
        assertThat(baos.toString(), containsString("out of stock"));
    }

    @Test
    public void testSelectItemInsufficientMoney() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.loadSlot(2, 0, 35, Item.Pepsi);
        vm.insertCoin(new Coin(25));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        Pair<List<Coin>, Item> changeItemPair = vm.selectItem(2);

        assertThat(changeItemPair, is(nullValue()));
        assertThat(baos.toString(), containsString("Insufficient money inserted"));
    }

    @Test
    public void testRefundCoins() {
        SimpleVendingMachine vm = new SimpleVendingMachine();
        vm.insertCoin(new Coin(10));
        vm.insertCoin(new Coin(25));
        List<Coin> coins = vm.refundCoins();
        assertThat(coins.size(), is(equalTo(2)));
        assertThat(coins.get(0).getValue(), is(equalTo(10)));
        assertThat(coins.get(1).getValue(), is(equalTo(25)));
    }
}