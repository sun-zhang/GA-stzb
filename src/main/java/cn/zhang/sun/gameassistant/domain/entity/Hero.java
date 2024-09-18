package cn.zhang.sun.gameassistant.domain.entity;

public class Hero {
    //编号，默认值为0，表示全部
    private int heroId = 0;
    private String heroName;
    private String heroPic;

    private String webHeroPic;
    //阵营分类，默认值为0，表示全部
    private int faction = 0;
    //星级，默认值为0，表示全部
    private int heroStarLevel = 0;
    //是否启用，默认值为true，表示启用
    private boolean enable = true;

    public Hero() {
    }

    // Getters and Setters
    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroPic() {
        return heroPic;
    }

    public void setHeroPic(String heroPic) {
        this.heroPic = heroPic;
    }

    public String getWebHeroPic() {
        return webHeroPic;
    }

    public void setWebHeroPic(String webHeroPic) {
        this.webHeroPic = webHeroPic;
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public int getHeroStarLevel() {
        return heroStarLevel;
    }

    public void setHeroStarLevel(int heroStarLevel) {
        this.heroStarLevel = heroStarLevel;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
