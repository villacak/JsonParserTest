package com.test.json.parser.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.json.parser.enums.PersonType;
import com.test.json.parser.model.Doctor;
import com.test.json.parser.model.Nurse;
import com.test.json.parser.model.Patient;
import com.test.json.parser.utils.JSONStreamParser;

import java.util.*;

/**
 * Created by klausvillaca on 2/4/17.
 */
public class StartParser {

    private static final String FILENAME = "smartward.json";
    private static final String CLASS_KEY = "class";

    public static void main(String[] args) {

        final List<Patient> patientList = new ArrayList<>();
        final List<Doctor> doctorList = new ArrayList<>();

        // Nurse need grouping by ward number
        Map<Integer, List<Nurse>> nurseMap = new HashMap<Integer, List<Nurse>>();

        final ObjectMapper mapper = new ObjectMapper();
        final JSONStreamParser jsonReader = new JSONStreamParser(FILENAME);

        Map<String, Object> jsonToken = null;

        while(jsonReader.hasNext()) {
            jsonToken = jsonReader.next();

            // Check if the token has the key string 'class'
            if (jsonToken.containsKey(CLASS_KEY)) {
                // Get the key
                final String tempClass = (String) jsonToken.get(CLASS_KEY);

                // Check for PersonType
                if (tempClass.contains(PersonType.DOCTOR.getType())) {
                    final Doctor tempDoctor = mapper.convertValue(jsonToken, Doctor.class);
                    doctorList.add(tempDoctor);
                } else if (tempClass.contains(PersonType.NURSE.getType())) {
                    final Nurse tempNurse = mapper.convertValue(jsonToken, Nurse.class);
                    nurseMap = populateNurseMap(tempNurse, nurseMap);
                } else {
                    // Patient
                    final Patient tempPatient = mapper.convertValue(jsonToken, Patient.class);
                    patientList.add(tempPatient);
                }
            }
        }


        // Sort Patients by first name
        patientList.sort(Comparator.comparing(Patient::getFirstName));

        // print Patients list
        System.out.println("Patient List in alphabetical order");
        for(Patient pojoPatient: patientList) {
            final StringBuilder sbTemp = new StringBuilder(pojoPatient.getFirstName())
                    .append(" ").append(pojoPatient.getFamilyName());
            System.out.println(sbTemp.toString());
        }


        // Sort Map by key (Ward Number)
        final Map<Integer, List<Nurse>> nurseMap2 = new HashMap<Integer, List<Nurse>>();
        nurseMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, List<Nurse>>comparingByKey())
                .forEachOrdered(x -> nurseMap2.put(x.getKey(), x.getValue()));
        nurseMap = null;

        // print Nurse list
        System.out.println();
        System.out.println();
        System.out.println("Nurse List grouped by Ward Number in ascending order with Nurses in alphabetical order");
        for (int wardNumberTemp: nurseMap2.keySet()) {
            final List<Nurse> tempNurseList = nurseMap2.get(wardNumberTemp);
            System.out.println();
            System.out.println("Ward #: " + wardNumberTemp);

            // Sort each list from each ward number
            tempNurseList.sort(Comparator.comparing(Patient::getFirstName));

            for (Nurse pojoNurse : tempNurseList) {
                final StringBuilder sbTemp = new StringBuilder(pojoNurse.getFirstName())
                        .append(" ").append(pojoNurse.getFamilyName());
                System.out.println(sbTemp.toString());
            }
        }


        // Sort Doctors by first name
        doctorList.sort(Comparator.comparing(Patient::getFirstName));

        // Print Doctors List
        System.out.println();
        System.out.println();
        System.out.println("Doctor List in alphabetical order with respective pagers");
        for(Doctor pojoDoctor: doctorList) {
            final StringBuilder sbTemp = new StringBuilder(pojoDoctor.getFirstName())
                    .append(" ").append(pojoDoctor.getFamilyName())
                    .append(": ").append(pojoDoctor.getPagerNumber());
            System.out.println(sbTemp.toString());
        }

    }


    /**
     * Add the new Nurse to the Map
     *
     * @param tempNurse
     * @param tempMap
     * @return
     */
    private static Map<Integer, List<Nurse>> populateNurseMap(final Nurse tempNurse,
                                                              final Map<Integer, List<Nurse>> tempMap) {
        final int tempWardNumber = tempNurse.getWardNumber();
        final List<Nurse> tempNurseList;
        if (tempMap.containsKey(tempWardNumber)) {
            tempMap.get(tempWardNumber).add(tempNurse);
        } else {
            tempNurseList = new ArrayList<>();
            tempNurseList.add(tempNurse);
            tempMap.put(tempWardNumber, tempNurseList);
        }
        return tempMap;
    }

}
