package com.example.aidldemoserver;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLAdditionService extends Service {
    private static final int MAX_MARKS = 100;
    private static final float PASSING_PERCENTAGE = 33.0f;
    List<Student> students = new ArrayList<>();

    public AIDLAdditionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IMyAidlAdditionInterface.Stub binder = new IMyAidlAdditionInterface.Stub() {
        @Override
        public int calculateAddition(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public int calculateSubtraction(int a, int b) throws RemoteException {
            return a - b;
        }

        @Override
        public int getRandomNumber() throws RemoteException {
            int random = (int) (Math.random() * 100);
            return random;
        }

        @Override
        public int sumOfArray(int[] intArray) throws RemoteException {
            int sum = 0;
            for (int i = 0; i < intArray.length; i++) {
                sum += intArray[i];
            }
            return sum;
        }

        @Override
        public String concateString(String[] strArray) throws RemoteException {
            return TextUtils.join(",", strArray);
        }

        @Override
        public boolean basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            return (anInt == (int) anInt) && (aLong == (Long) aLong) && (aBoolean == (Boolean) aBoolean) && (aFloat == (Float) aFloat) && (aDouble == (Double) aDouble) && (aString == (String) aString);
        }

        @Override
        public String getStudentDetails(Student student) {
            List<Marks> marksList = student.getMarks();
            StringBuilder studentStrBuilder = new StringBuilder();

            studentStrBuilder.append("First Name: " + student.getFname());
            studentStrBuilder.append(", Last Name: " + student.getLname());
            studentStrBuilder.append(", RollNo: " + student.getRollno());
            studentStrBuilder.append(", Address: " + student.getAddress());
            studentStrBuilder.append(", PhoneNo: " + student.getPhoneno());
            studentStrBuilder.append(", Marks: ");

            for (Marks marks : marksList) {
                studentStrBuilder.append(marks.getSubjectName()).append(": ").append(marks.getMark()).append(" ");
            }

            return studentStrBuilder.toString();
        }

        @Override
        public Student createStudent() {
            List<Marks> marksList = new ArrayList<>();
            marksList.add(new Marks("Maths", 90));
            marksList.add(new Marks("Science", 85));
            marksList.add(new Marks("English", 95));

            Student student = new Student("Divya", "Parmar", 1234, 1234567890, "Rajkot,Gujarat", marksList);
            students.add(student);
            return student;
        }

        @Override
        public String getStudentDetails1() throws RemoteException {
            List<String> strings = new ArrayList<>();
            for (Student s : students) {
                strings.add(s.toString());
            }
            return strings.toString();
        }

        @Override
        public Student createStudent1(Student student) throws RemoteException {
            students.add(student);
            return student;
        }

        @Override
        public Result getResult(Student student) throws RemoteException {
            float percentage = calculatePercentage(student.getMarks());

            boolean passed = determinePassOrFail(percentage);

            return new Result(student.getFname(), student.getLname(), student.getRollno(), passed, percentage);
        }

        @Override
        public Student createStudentFromBundle(Bundle bundle) throws RemoteException {

            bundle.setClassLoader(Student.class.getClassLoader());

            Student obj = bundle.getParcelable("Student");

            List<Marks> marksList = obj.getMarks();
            if (marksList != null && marksList.size() > 0) {
                for (Marks mark : marksList) {
                    Log.e("ServiceServer", "Subject : " + mark.getSubjectName() + ", Mark : " + mark.getMark());
                }
            }
            return obj;
        }

        @Override
        public String sendStudents(List<Student> listOfStudents) throws RemoteException {
            StringBuilder result = new StringBuilder();
            for (Student student : listOfStudents) {
                result.append(student.getFname()).append(" ").append(student.getLname()).append(", ");
            }
            return result.toString();
        }

        @Override
        public int sendStudentEnum(EnumStudent enumStudent) throws RemoteException {
            Log.d("ans", "" + enumStudent.getId());
            return enumStudent.getId();
        }


    };

    private boolean determinePassOrFail(float percentage) {
        return percentage >= PASSING_PERCENTAGE;
    }

    private float calculatePercentage(List<Marks> marksList) {
        float totalMarks = 0;
        for (Marks marks : marksList) {
            totalMarks += marks.getMark();
        }
        float percentage = (totalMarks / (marksList.size() * MAX_MARKS)) * 100;
        return percentage;

    }
}