package com.example;

import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;


public class Foo {

    public static void testComponentExtension(){

        Observation ob = new Observation();
        ob.setStatus(Observation.ObservationStatus.REGISTERED);
        ob.addComponent();
        ob.getComponentFirstRep().setValue(new StringType("some value")).setOffset(new IntegerType(26));
        IParser p = FhirContext.forDstu3().newXmlParser().setPrettyPrint(true);
        String messageString = p.encodeResourceToString(ob);

        System.out.println(messageString);

    }

    public static void testResourceExtension(){
/*
        ExtObservation ob = new ExtObservation();
        ob.setStatus(ExtObservation.ObservationStatus.REGISTERED);
        ob.setMyEcgData(new StringType("place for some ECG data"));
        IParser p = FhirContext.forDstu3().newXmlParser().setPrettyPrint(true);
        String messageString = p.encodeResourceToString(ob);

        System.out.println(messageString);*/

    }

    public static void main(String[] args) {

    Foo.testComponentExtension();

    }
}
