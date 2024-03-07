package com.example.aidldemoserver;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLStudentService extends Service {
    private static final int MAX_MARKS = 100;
    private static final float PASSING_PERCENTAGE = 33.0f;


    public AIDLStudentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    List<Student> students = new ArrayList<>();

    private final IStudentInterface.Stub binder = new IStudentInterface.Stub() {
        @Override
        public String getStudentDetails(Student student) throws RemoteException {
            List<Marks> marksList = student.getMarks();
            StringBuilder marksStringBuilder = new StringBuilder();

            for (Marks marks : marksList) {
                marksStringBuilder.append(marks.getSubjectName()).append(": ").append(marks.getMark()).append(" ");
            }

            String allMarks = marksStringBuilder.toString();

            return "First Name: " + student.getFname() + ", Last Name: " + student.getLname() + ", RollNo: " + student.getRollno() + ", Address: " + student.getAddress() + ", PhoneNo: " + student.getPhoneno() + ", Marks: " + allMarks;
        }

        @Override
        public Student createStudent() throws RemoteException {
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
            List<String> strings = new ArrayList<String>();
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