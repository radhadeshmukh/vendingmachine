package com.test.vending.vo;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CoinTest {

    @Test
    public void test_CompareTo_for_larger_value() {
        Coin coin = new Coin(10);
        Coin largerValueCoin = new Coin(25);
        assertThat(coin.compareTo(largerValueCoin), is(lessThan(0)));
    }

    @Test
    public void test_CompareTo_for_smaller_value() {
        Coin coin = new Coin(10);
        Coin smallerValueCoin = new Coin(5);
        assertThat(coin.compareTo(smallerValueCoin), is(greaterThan(0)));
    }

    @Test
    public void test_CompareTo_for_equal_value() {
        Coin coin = new Coin(10);
        Coin equalCoin = new Coin(10);
        assertThat(coin.compareTo(equalCoin), is(equalTo(0)));
    }


}