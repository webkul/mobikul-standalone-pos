package com.webkul.mobikul.mobikulstandalonepos.mail;

import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by anchit.makkar on 19/12/17.
 */

public class SMTPAuthenticator extends Authenticator {
    public SMTPAuthenticator() {

        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = ApplicationConstants.USERNAME_FOR_SMTP;
        String password = ApplicationConstants.PASSWORD_FOR_SMTP;
        if ((username != null) && (username.length() > 0) && (password != null)
                && (password.length() > 0)) {

            return new PasswordAuthentication(username, password);
        }

        return null;
    }
}
