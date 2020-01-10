package com.f22labs.instalikefragmenttransaction.Adapter;

import java.io.File;
import java.util.ArrayList;

public class DosyaArama {

    public static ArrayList<String> getDosyaYolu(String klasör)
    {
        ArrayList<String> Dosyalarim = new ArrayList<>();

        File file = new File(klasör);

        File[] files = file.listFiles();
        if(files != null) {

            for (int i = 0; i < files.length; i++) {

                if (files[i].isFile()) {
                    ArrayList<String> name = new ArrayList<>();

                    name.add(files[i].getAbsolutePath());

                    for (int j = 0; j < name.size(); j++) {
                        int son = name.get(j).lastIndexOf(".");
                        String string = name.get(j).substring(son);

                        if (string.equals(".jpg") || string.equals(".png") || string.equals(".mp4")) {
                            Dosyalarim.add(name.get(j));
                        }
                    }

                }
            }
        }


        return Dosyalarim;

    }

}

