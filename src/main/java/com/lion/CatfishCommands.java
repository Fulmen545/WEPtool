package com.lion;

import com.wirecard.catfish.data.BrandXml;
import com.wirecard.catfish.domain.data.Money;
import com.wirecard.catfish.enums.LoadAccountUseCaseType;
import com.wirecard.catfish.mocks.EkycMockService;
import com.wirecard.catfish.responses.callcenter.CallCenterBulkCreatedCardDetailsResponse;
import com.wirecard.catfish.services.callcenter.*;
import com.wirecard.catfish.entities.User;
import com.wirecard.catfish.services.horus.UnlockChildAccountService;
import com.wirecard.horus.data.callcenter.customer.EditAddressDetails;
import com.wirecard.horus.data.callcenter.customer.personverificationstatus.Status;

public class CatfishCommands {
    public User user;

    CatfishCommands(String number, String pwd) {
        user = LionUserUtil
                .getUserWithDeterminedAliasType(new com.wirecard.catfish.entities.Brand("boon"),
                        "+" + number, pwd);
    }


    public void lockAccount() {
//        User user = LionUserUtil
//                .getUserWithDeterminedAliasType(new com.wirecard.catfish.entities.Brand("boon"),
//                        "+"+number, pwd);
        CallCenterLockCustomerService service = new CallCenterLockCustomerService();
        String log = service.lockCustomer(user).responseAsString();
    }

    public void unlockAccount() {
        CallCenterCustomerCountersService cccs = new CallCenterCustomerCountersService();
        cccs.reset(user, CallCenterCustomerCountersService.CounterType.NUM_FAILED_LOGINS);
        cccs.reset(user, CallCenterCustomerCountersService.CounterType.NUM_FAILED_SECURITY_ANSWERS);
        CallCenterUnlockUserService service = new CallCenterUnlockUserService();
        CallCenterUnlockCustomerService service1 = new CallCenterUnlockCustomerService();
        service.unlockUser(user);
        service1.unlockCustomer(user);
    }

    public void closeAccount() {

        try {
            Money amount = new Helper().getUserInfo(user).getAvailableAmount();
            if (amount.isPositive()) {
                CallCenterUnloadAccountService unloadAccountService = new CallCenterUnloadAccountService();
                unloadAccountService.unloadAccount(user,
                        amount,
                        LoadAccountUseCaseType.LOAD_ACCOUNT_MANUAL,
                        BrandXmlUtil.getManualLoadSystemId(new BrandXml(user.getBrand()),
                                amount.getCurrency()));
            } else if (amount.isNegative()) {
                CallCenterLoadAccountService loadAccountService = new CallCenterLoadAccountService();
                loadAccountService.loadAccount(user,
                        LoadAccountUseCaseType
                                .LOAD_ACCOUNT_MANUAL,
                        amount.negate(),
                        BrandXmlUtil.getManualLoadSystemId(new BrandXml(user.getBrand()),
                                amount.getCurrency()));
            }
        } catch (NullPointerException e) {
            System.out.println("user does not have available amount, trying to close customer without clearing his balance...");
        }

        CallCenterCloseCustomerService service = new CallCenterCloseCustomerService();
        service.closeCustomer(user);
    }

    public void upgradeAccount() {
        EditAddressDetails editAddressDetails = new EditAddressDetails();
        editAddressDetails.setCustomerAddress(new Helper().getFilledAddress());
        new CallCenterPersonalVerificationService()
                .editPersonalVerificationStatus(user, Status.VERIFIED);
        new CallCenterVerifyAddressService().verifyAddress(user, editAddressDetails);
    }

}
