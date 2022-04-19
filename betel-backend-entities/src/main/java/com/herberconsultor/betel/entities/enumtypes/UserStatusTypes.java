/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities.enumtypes;

/**
 *
 * @author Usuario
 */
public enum UserStatusTypes {
    CREATED,PENDIND_VERIFY,APPROVED,REJECTED;
    
    public static UserStatusTypes convertFromString(String value) {
        switch(value) {
            case "CREATED":
                return UserStatusTypes.CREATED;
            case "PENDIND_VERIFY":
                return UserStatusTypes.PENDIND_VERIFY;
            case "APPROVED":
                return UserStatusTypes.APPROVED;
            case "REJECTED":
                return UserStatusTypes.REJECTED;
            default:
                return null;
        }
    }
}
