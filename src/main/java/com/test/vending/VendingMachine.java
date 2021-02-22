package com.test.vending;

import com.test.vending.vo.Coin;
import com.test.vending.vo.Item;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface VendingMachine {

    //1. Accepts coins of 1,5,10,25 Cents i.e. penny, nickel, dime, and quarter.
    void insertCoin(Coin coin);

    //2. Allow user to select products Coke(25), Pepsi(35), Soda(45)
    //4. Return selected product and remaining change if any.
    Pair<List<Coin>, Item> selectItem(int slotNumber);

    //3. Allow user to take refund by cancelling the request.
    List<Coin> refundCoins();

    //5. Allow reset operation for vending machine supplier.
    void reset();

}
