package org.example.springappportfolio.mappers;

import org.example.springappportfolio.dto.ContactDto;
import org.example.springappportfolio.models.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactDto toDto(Contact contact) {

        return new ContactDto(
                contact.getId(),
                contact.getContactType(),
                contact.getContactValue(),
                contact.getDisplayOrder(),
                contact.getIsVisible()
        );

    }

}
