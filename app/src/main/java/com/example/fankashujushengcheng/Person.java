package com.example.fankashujushengcheng;

/**
 * Created by yxl on 17/11/29.
 */

public class Person {
    private String personName;
    private String personWorkerId;

    public Person(String personName, String personWorkerId) {
        this.personName = personName;
        this.personWorkerId = personWorkerId;
    }

    public String getPersonName() {

        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonWorkerId() {
        return personWorkerId;
    }

    public void setPersonWorkerId(String personWorkerId) {
        this.personWorkerId = personWorkerId;
    }
}
