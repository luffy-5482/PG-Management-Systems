package com.parent.pg.service;

import java.util.List;

import com.parent.pg.dto.ContactPersonRequest;
import com.parent.pg.dto.ContactPersonResponse;

public interface ContactPersonService {

    ContactPersonResponse createContact(ContactPersonRequest req);

    ContactPersonResponse updateContact(Long id, ContactPersonRequest req);

    void deleteContact(Long id);

    List<ContactPersonResponse> getContactsByPg(Long pgId);
}
