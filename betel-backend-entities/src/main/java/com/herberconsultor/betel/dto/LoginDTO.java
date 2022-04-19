/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.dto;

import com.herberconsultor.betel.entities.User;
import java.io.Serializable;

/**
 *
 * @author Herber Consultor
 */
public class LoginDTO implements Serializable{
    private User user;
    private Integer codeResult;
    private String messageResult;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCodeResult() {
        return codeResult;
    }

    public void setCodeResult(Integer codeResult) {
        this.codeResult = codeResult;
    }

    public String getMessageResult() {
        return messageResult;
    }

    public void setMessageResult(String messageResult) {
        this.messageResult = messageResult;
    }
    
    
    
}
