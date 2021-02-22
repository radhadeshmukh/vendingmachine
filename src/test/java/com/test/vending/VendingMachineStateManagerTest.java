package com.test.vending;

import com.test.vending.vo.Event;
import com.test.vending.vo.State;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class VendingMachineStateManagerTest {

    @Test
    public void test_new_VendingStateManager_READY_state() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        assertThat(vsm.getCurrentState(), is(equalTo(State.READY)));
    }

    @Test
    public void test_READY_state_COIN_INSERTED_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ACCEPTING_COINS_state_COIN_INSERTED_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.COIN_INSERTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ACCEPTING_COINS_state_REFUND_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.REFUND_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.REFUND)));
    }

    @Test
    public void test_ITEM_SELECTED_state_INSUFFICIENT_MONEY_INSERTED_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.ITEM_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ITEM_SELECTED)));
        vsm.onEvent(Event.INSUFFICIENT_MONEY_INSERTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ITEM_SELECTED_state_INSUFFICIENT_CHANGE_IN_MACHINE_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.ITEM_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ITEM_SELECTED)));
        vsm.onEvent(Event.INSUFFICIENT_MONEY_INSERTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ITEM_SELECTED_state_INVALID_ITEM_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.ITEM_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ITEM_SELECTED)));
        vsm.onEvent(Event.INVALID_ITEM);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ITEM_SELECTED_state_OUT_OF_STOCK_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.ITEM_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ITEM_SELECTED)));
        vsm.onEvent(Event.OUT_OF_STOCK);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ACCEPTING_COINS)));
    }

    @Test
    public void test_ITEM_SELECTED_state_DISPENSE_event() {
        VendingMachineStateManager vsm = new VendingMachineStateManager();
        vsm.onEvent(Event.COIN_INSERTED);
        vsm.onEvent(Event.ITEM_SELECTED);
        assertThat(vsm.getCurrentState(), is(equalTo(State.ITEM_SELECTED)));
        vsm.onEvent(Event.DISPENSE);
        assertThat(vsm.getCurrentState(), is(equalTo(State.READY)));
    }
}