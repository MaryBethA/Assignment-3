/**
 * Assignment 3
 * ReadStakeholderSer.java
 * Mary-Beth Agulhas 
 * 219082367
 * 2 June 2021
*/
package za.ac.cput.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadStakeholderSer {
//2a. Make Seperate Array Lists for Customer and Supplier
ObjectInputStream ReadFile;    
ArrayList<Customer> customerClass= new ArrayList<>();
ArrayList<Supplier> supplierClass= new ArrayList<>();
BufferedWriter bufferedWriter;
FileWriter write;

    public void openFile(){
        try{
            ReadFile = new ObjectInputStream( new FileInputStream( "stakeholder.ser" ) ); 
            System.out.println("*** Ser file opened for reading  ***");               
        }
        catch (IOException ioe){
            System.out.println("Error opening ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    //2a. Read Ser File
    public void readFile(){
    String customerclass ="Customer";
    String supplierclass = "Supplier";
        try{
           while(true){
               Object object = ReadFile.readObject();
               
               if ( object.getClass().getSimpleName().equals(customerclass)){
                   customerClass.add((Customer)object);
                   
               } else if ( object.getClass().getSimpleName().equals(supplierclass)){
                   supplierClass.add((Supplier)object);
                   
               } else {
                   System.out.println("Unresponsive");
               }
           } 
        }
        catch (EOFException eofe) {
            System.out.println("End of Ser File");
        }
        catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: "+ ioe);
        }
        catch (IOException ioe) {
            System.out.println("Error reading ser file: "+ ioe);
        }
        sortingCustomers();
        sortingSuppliers();
    }
    public void closeFile(){
        try{
            ReadFile.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("Error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    
    
    
    //2b Sorting Customers
    public void sortingCustomers(){
        String[] stkID = new String[customerClass.size()];
        ArrayList<Customer> sortedID= new ArrayList<>();
        
        
        for (int i = 0; i < customerClass.size(); i++) {
            stkID[i] = customerClass.get(i).getStHolderId();
        }
        
        Arrays.sort(stkID);
        
        for (int i = 0; i < customerClass.size(); i++) {
            for (int m = 0; m < customerClass.size(); m++) {
                if (stkID[i].equals(customerClass.get(m).getStHolderId())){
                    sortedID.add(customerClass.get(m));
                }
            }
        }
        customerClass = sortedID;
    }
    
    
    //2c Determining Customer Age
    public int determineAges(String dob){
       LocalDate born = LocalDate.parse(dob);
       LocalDate currentDate = LocalDate.now();
       int age = Period.between(born, currentDate).getYears();
       return age;
    }
    
    
    //2d Formatting Date of Birth
    public String formatDateOfBirth(Customer dob){
        LocalDate formatDob = LocalDate.parse(dob.getDateOfBirth());
        DateTimeFormatter changeDobFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return formatDob.format(changeDobFormat);
    }
    
    
    
     //2e Displaying Sorted Customer Array List in Text File
    public void displayCustomerTxt(){
        try{
            write= new FileWriter("customerOutFile.txt");
            bufferedWriter = new BufferedWriter(write);
            bufferedWriter.write(String.format("%s\n","======================CUSTOMERS=============================="));
            bufferedWriter.write(String.format("%-5s%-10s%-10s\t%-15s\t%-10s\n", "ID","Name","Surname","Date of Birth","Age"));
            bufferedWriter.write(String.format("%s\n","============================================================="));
            for (int i = 0; i < customerClass.size(); i++) {
            bufferedWriter.write(String.format("%-5s%-10s%-10s\t%-15s\t%-10s \n", customerClass.get(i).getStHolderId(), customerClass.get(i).getFirstName(), customerClass.get(i).getSurName(), formatDateOfBirth(customerClass.get(i)),determineAges(customerClass.get(i).getDateOfBirth())));
            }
            bufferedWriter.write(String.format("%s\n"," "));
            bufferedWriter.write(String.format("%s\n"," "));
            bufferedWriter.write(String.format("%s\n",Rent()));
            
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bufferedWriter.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("Error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    
    //2f Determing who can and can't rent
    public String Rent(){
        int r = 0;
        int cr = 0;
        
        for (int i = 0; i < customerClass.size(); i++) {
            if (customerClass.get(i).getCanRent()){
                r++;
            }else {
                cr++;
            }
        }
        String renting = "Number of customers who can rent : "+ '\t' + r + '\n' + "Number of customers who cannot rent : "+ '\t' + cr;
        return renting;
    }
    
 
    
    //3a Sorting Supplier Array List
    public void sortingSuppliers(){
        String[] sortNames = new String[supplierClass.size()];
        ArrayList<Supplier> sortedNames= new ArrayList<>();
        
        for (int k = 0; k < supplierClass.size(); k++) {
            sortNames[k] = supplierClass.get(k).getName();
        }
        
        Arrays.sort(sortNames);
        
        for (int k = 0; k < supplierClass.size(); k++) {
            for (int l = 0; l < supplierClass.size(); l++) {
                if (sortNames[k].equals(supplierClass.get(l).getName())){
                    sortedNames.add(supplierClass.get(l));
                }
            }
        }
        supplierClass = sortedNames;
    }
   //3b Displaying Supplier Array List in a Text File
   public void displaySupplierTxt(){
        try{
            write = new FileWriter("supplierOutFile.txt");
            bufferedWriter = new BufferedWriter(write);
            bufferedWriter.write(String.format("%s\n","=========================SUPPLIERS==============================="));
            bufferedWriter.write(String.format("%-5s\t%-20s\t%-10s\t%-15s\n", "ID","Name","Prod Type","Description"));
            bufferedWriter.write(String.format("%s\n","================================================================="));
            for (int i = 0; i < supplierClass.size(); i++) {
            bufferedWriter.write(String.format("%-5s\t%-20s\t%-10s\t%-15s\n", supplierClass.get(i).getStHolderId(), supplierClass.get(i).getName(), supplierClass.get(i).getProductType(),supplierClass.get(i).getProductDescription()));
            }
            
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bufferedWriter.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("Error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }  
    
      
    
public static void main(String args[])  {
ReadStakeholderSer obj=new ReadStakeholderSer(); 
obj.openFile();
obj.readFile();
obj.displayCustomerTxt();
obj.displaySupplierTxt();
obj.closeFile();

     }    
     
}
