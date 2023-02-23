package com.xplaptop.setting;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingBag;

import java.util.List;

public class CurrencySettingBag extends SettingBag {
    public CurrencySettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getCurrencySymbol() {
        return super.getValue("CURRENCY_SYMBOL");
    }

    public String getCurrencySymbolPosition() {
        return super.getValue("CURRENCY_SYMBOL_POSITION");
    }

    public String getDecimalPointType() {
        return super.getValue("DECIMAL_POINT_TYPE");
    }

    public String getThousandsPointType() {
        return super.getValue("THOUSANDS_POINT_TYPE");
    }

    public int getCurrencyDigits() {
        return Integer.parseInt(super.getValue("CURRENCY_DIGITS"));
    }
}
