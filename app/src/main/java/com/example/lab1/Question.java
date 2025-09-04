package com.example.lab1;
import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    public String prompt;
    public List<String> options; // tamaño 4
    public int correctIndex;     // 0..3
}