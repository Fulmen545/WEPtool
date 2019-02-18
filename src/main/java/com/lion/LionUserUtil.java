package com.lion;

import com.wirecard.catfish.entities.Brand;
import com.wirecard.catfish.entities.User;
import com.wirecard.catfish.enums.LoginAliasType;
import com.wirecard.catfish.requests.callcenter.CustomerDataField;
import com.wirecard.catfish.responses.callcenter.CallCenterSearchChildAccountResponse;
import com.wirecard.catfish.services.callcenter.CallCenterEditCustomerService;
import com.wirecard.catfish.services.callcenter.CallCenterSearchChildAccountsService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class LionUserUtil {

    private static final List<Pair<String, LoginAliasType>> ALIAS_TYPES = Arrays.asList(
            Pair.of("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
                    "-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:" +
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
                    "-\\x7f])+)\\])", LoginAliasType.EMAIL),
            Pair.of("^\\+[0-9]{0,18}", LoginAliasType.MSISDN),
            Pair.of("^[0-9]{13}$", LoginAliasType.LOADING_NUMBER)
    );

    private LionUserUtil() {
    }

    public static final User getUserByLoginName(Brand brandName, String loginName, String password) {
        User user = new User();
        user.setBrand(brandName);
        user.setAliasType(LoginAliasType.LOGIN_NAME);
        user.setLoginName(loginName);
        user.setPassword(password);
        return user;
    }

    public static final User getUserByLoginName(Brand brandName, String loginName) {
        User user = new User();
        user.setBrand(brandName);
        user.setAliasType(LoginAliasType.LOGIN_NAME);
        user.setLoginName(loginName);
        return user;
    }

    public static final User getUserByLoadingNumber(Brand brandName, String loadingNumber, String password) {
        User user = new User();
        user.setBrand(brandName);
        user.setAliasType(LoginAliasType.LOADING_NUMBER);
        user.setLoadingNumber(loadingNumber);
        user.setPassword(password);
        return user;
    }

    public static final User getUserWithDeterminedAliasType(Brand brandName, String loginAlias, String password) {
        LoginAliasType loginAliasType = determineAliasType(loginAlias);
        User user = new User()
                .setBrand(brandName)
                .setPassword(password)
                .setAliasType(loginAliasType);

        switch (loginAliasType) {
            case EMAIL:
                return user.setEmail(loginAlias);
            case MSISDN:
                return user.setMobileNumber(loginAlias);
            case LOGIN_NAME:
                return user.setLoginName(loginAlias);
            case LOADING_NUMBER:
                return user.setLoadingNumber(loginAlias);
            case CARD_PAN:
                user.setCreditCardNumber(loginAlias);
                return user;
            default:
                throw new RuntimeException(String.format("Alias type %s not mapped.", loginAliasType.toString()));
        }
    }

    public static int getChildAccountNumber(User user) {
        CallCenterSearchChildAccountsService ccsca = new CallCenterSearchChildAccountsService();
        CallCenterSearchChildAccountResponse response = ccsca.searchChildAccounts(user);
        Assert.assertEquals(response.getStatus(), 200);
        return response.getBean().getChildren().size();
    }

    public static void setPhoneNumberForUser(User user, String number) {
        CallCenterEditCustomerService ccecs = new CallCenterEditCustomerService();
        ccecs.update(user, CustomerDataField.mobileNumber(number));
    }

    private static LoginAliasType determineAliasType(final String loginAlias) {
        return ALIAS_TYPES.stream().filter(pair -> Pattern.compile(pair.getLeft()).matcher(loginAlias).matches())
                .findFirst().map(Pair::getRight).orElse(LoginAliasType.LOGIN_NAME);
    }
}
