package com.lion;

import com.wirecard.catfish.data.BrandXml;
import com.wirecard.catfish.enums.Currency;

public class BrandXmlUtil {

    private BrandXmlUtil() {
    }

    public static String getManualLoadSystemId(BrandXml brandXml, Currency currency) {
        String systemId = brandXml.getEntity
                ("/brand/manual-booking-types/manual-booking-type[@is-credit='true']/accounts/account[@account-type" +
                        "='manual_load' and " +
                        "contains(@systemId, '" + currency + "')" +
                        "]/@systemId");
        if (systemId.isEmpty()) {
            systemId = brandXml.getEntity("//load-unload-accounts/system-id[contains(@name, '" + currency + "')" +
                    "and (contains(@name, 'manual') or contains(@name, '" + brandXml.getBrandName() + "'))]/@name");
        }
        return systemId.isEmpty() ? brandXml.getManualLoadSystemId() : systemId;
    }
}
