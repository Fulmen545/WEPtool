package com.lion;

import com.wirecard.catfish.beans.userresponse.HorusUserInfoResponse;
import com.wirecard.catfish.entities.User;
import com.wirecard.catfish.services.horus.HorusUserInfoService;
import com.wirecard.horus.data.callcenter.support.Address;

public class Helper {

    public Address getFilledAddress(){
        Address address = new Address();
        address.setCity("Munich");
        address.setCountry("GE");
        address.setZipCode("00444");
        address.setStreet("Haupt");
        return  address;
    }

    public HorusUserInfoResponse getUserInfo(User user) {
        return new HorusUserInfoService().getUserInfo(user);
    }
}
