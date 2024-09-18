package cn.zhang.sun.gameassistant.domain.dao;

import cn.zhang.sun.gameassistant.domain.entity.Hero;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HeroRowMapper implements RowMapper<Hero> {

    @Override
    public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hero hero = new Hero();
        hero.setHeroId(rs.getInt("heroId"));
        hero.setHeroName(rs.getString("heroName"));
        hero.setHeroPic(rs.getString("heroPic"));
        hero.setWebHeroPic(rs.getString("webPicPath"));
        hero.setFaction(rs.getInt("heroFaction"));
        hero.setHeroStarLevel(rs.getInt("heroStarLevel"));
        hero.setEnable(rs.getBoolean("enable"));
        return hero;
    }
}
