package com.googlecode.talkmyphone.actions;

import java.util.ArrayList;

import com.googlecode.talkmyphone.XmppService;
import com.googlecode.talkmyphone.contacts.Contact;
import com.googlecode.talkmyphone.contacts.ContactAddress;
import com.googlecode.talkmyphone.contacts.ContactsManager;
import com.googlecode.talkmyphone.contacts.Phone;

import android.content.Context;
import android.content.Intent;

public class NotifyMatchingContactsAction extends Action {

    @Override
    public void execute(Context context, Intent intent) {

        String searchedText = intent.getStringExtra("args");

        ArrayList<Contact> contacts = ContactsManager.getMatchingContacts(searchedText);

        if (contacts.size() > 0) {
            for (Contact contact : contacts) {
                StringBuilder strContact = new StringBuilder();
                strContact.append(XmppService.makeBold(contact.name));

                ArrayList<Phone> mobilePhones = ContactsManager.getPhones(contact.id);
                if (mobilePhones.size() > 0) {
                    strContact.append("\r\n" + XmppService.makeItalic("Phones"));
                    for (Phone phone : mobilePhones) {
                        strContact.append("\r\n" + phone.label + " - " + phone.cleanNumber);
                    }
                }

                ArrayList<ContactAddress> emails = ContactsManager.getEmailAddresses(contact.id);
                if (emails.size() > 0) {
                    strContact.append("\r\n" + XmppService.makeItalic("Emails"));
                    for (ContactAddress email : emails) {
                        strContact.append("\r\n" + email.label + " - " + email.address);
                    }
                }

                ArrayList<ContactAddress> addresses = ContactsManager.getPostalAddresses(contact.id);
                if (addresses.size() > 0) {
                    strContact.append("\r\n" + XmppService.makeItalic("Addresses"));
                    for (ContactAddress address : addresses) {
                        strContact.append("\r\n" + address.label + " - " + address.address);
                    }
                }
                XmppService.getInstance().send(strContact.toString() + "\r\n");
            }
        } else {
            XmppService.getInstance().send("No match for \"" + searchedText + "\"");
        }
    }

}
