package com.costumers.lawyer.entities;

/**
 * Created by stiven on 3/15/2016.
 */
public class PersonAdapter {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        this.Document = document;
    }

    private String Name;
    private String LastName;
    private String Document;

    public String getIdPerson() {
        return IdPerson;
    }

    public void setIdPerson(String idPerson) {
        IdPerson = idPerson;
    }

    public String getNameLastName() {
        return NameLastName;
    }

    public void setNameLastName(String nameLastName) {
        NameLastName = nameLastName;
    }

    private String IdPerson;
    private String NameLastName;

    public String getLastContact() {
        return LastContact;
    }

    public void setLastContact(String lastContact) {
        LastContact = lastContact;
    }

    private String LastContact;

    public PersonAdapter()
    {

    }

    public PersonAdapter(String Name,String LastName,String Document,String LastContact, String IdPerson, String NameLastName)
    {
        this.Name=Name;
        this.LastName=LastName;
        this.Document=Document;
        this.LastContact=LastContact;
        this.IdPerson=IdPerson;
        this.NameLastName=NameLastName;

    }



}
