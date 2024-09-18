package cn.zhang.sun.gameassistant.tools;

import cn.zhang.sun.gameassistant.domain.dao.HeroDao;
import cn.zhang.sun.gameassistant.domain.entity.Hero;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PicImporter {

    public static int extractFileId(String fileName) {
        String regex = "card_medium_(\\d+)\\.jpg";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid file name format");
        }
    }


    public static List<File> getFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    public List<Hero> processHeroes(String mainDir, String specialDir) {
        List<File> mainFiles = PicImporter.getFiles(mainDir);
        List<File> specialFiles = PicImporter.getFiles(specialDir);

        Set<Integer> specialFileIds = new HashSet<>();
        for (File file : specialFiles) {
            specialFileIds.add(PicImporter.extractFileId(file.getName()));
        }

        List<Hero> heroes = new ArrayList<>();
        for (File file : mainFiles) {
            int fileId = PicImporter.extractFileId(file.getName());
            Hero hero = new Hero();
            hero.setHeroId(fileId);
            hero.setHeroName(file.getName());
            hero.setHeroPic(file.getAbsolutePath());
            hero.setHeroStarLevel(specialFileIds.contains(fileId) ? 5 : 3);
            hero.setEnable(true);
            heroes.add(hero);
        }
        return heroes;
    }

    public static void main(String[] args) {

        String mainDir = "/Users/Sam/Workspace/AIGC/GameAssistant/bin/pic";
        String specialDir = "/Users/Sam/Workspace/AIGC/GameAssistant/bin/pic5star";
        PicImporter picImporter = new PicImporter();
        List<Hero> heroes = picImporter.processHeroes(mainDir, specialDir);
        HeroDao heroDao = new HeroDao();
        heroDao.saveHeroes(heroes);
    }

}

