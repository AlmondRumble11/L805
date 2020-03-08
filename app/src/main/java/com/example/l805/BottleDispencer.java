package com.example.l805;

import java.util.ArrayList;

class BottleDispencer {
    public double raha = 0;
    private int tarkistus;
    public ArrayList<Bottle> bottle;
    private static BottleDispencer bd = new BottleDispencer();

    public BottleDispencer() {
    }

    public void lisaaPullot() {
        ArrayList<Bottle> bottle = new ArrayList<Bottle>();
        Bottle nimi1 = new Bottle("Pepsi Max", "Pepsi", 0.3, 1.8, 0.5);
        bottle.add(nimi1);
        Bottle nimi2 = new Bottle("Pepsi Max", "Pepsi", 0.3, 2.2, 1.5);
        bottle.add(nimi2);
        Bottle nimi3 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3, 2.0, 0.5);
        bottle.add(nimi3);
        Bottle nimi4 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3, 2.5, 1.5);
        bottle.add(nimi4);
        Bottle nimi5 = new Bottle("Fanta Zero", "Coca-Cola", 0.3, 1.95, 0.5);
        bottle.add(nimi5);
        setList(bottle);
    }

    public static BottleDispencer getInstance() {
        return bd;

    }

    public ArrayList<Bottle> getList() {
        return bottle;
    }

    public void setList(ArrayList<Bottle> l) {
        bottle = l;
    }

    //Rahan tallennus on tehty tyhjästi sillä alunperin oli loopin sisällä new BottleDispenser() joten se aina nollas rahat
    public void addMoney(double x) {
        raha = raha + x;
        System.out.println("Klink! Added more money!");
        //return money;
    }

    public int buyBottle(String pullo, String koko) {

        tarkistus = 0;
        try {
            if (bottle.size() == 0) {
                System.out.println("No more bottles!");
            }

            else {

                for (int i = 0; i < bottle.size(); i++) {
                    if ((bottle.get(i).getName() == pullo) && (bottle.get(i).getSize() == Double.valueOf(koko))) {
                        tarkistus = 2;
                        if ((bottle.get(i).getPrice() <= raha)) {
                            System.out.println(bottle.get(i).getName() + i);
                            raha = raha - bottle.get(i).getPrice();
                            bottle.remove(i);
                            tarkistus = 1;
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Wrong choice");
        }
        return tarkistus;

    }

    public void showBottles() {
        int n = 0;
        for (Bottle i : bottle)
        {
            System.out.println((n+1) +". Name: " + i.getName());
            System.out.println("	Size: " + i.getSize() + "	Price: " + i.getPrice());
            n++;
        }
    }
    public double getMoney(){
        return raha;

    }
    public double returnMoney() {
        System.out.printf("Klink klink. Money came out! You got %.2f€ back\n", raha);
        raha = 0;
        return raha;
    }
}
