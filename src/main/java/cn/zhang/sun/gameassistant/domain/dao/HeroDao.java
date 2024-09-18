package cn.zhang.sun.gameassistant.domain.dao;

import cn.zhang.sun.gameassistant.domain.dto.HeroDTO;
import cn.zhang.sun.gameassistant.domain.entity.Hero;
import com.mysql.cj.protocol.Resultset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HeroDao extends BaseDao{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Hero> getHeroes(HeroDTO heroDTO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM heroes h where 1=1 and h.enable = 1");
        List<Object> params = new ArrayList<>();

        if (heroDTO.getHero().getHeroId() > 0) {
            sql.append(" AND h.heroId = ?");
            params.add(heroDTO.getHero().getHeroId());
        }
        if (StringUtils.hasText(heroDTO.getHero().getHeroName())) {
            sql.append(" AND h.heroName like ?");
            params.add("%" + heroDTO.getHero().getHeroName() + "%");
        }
        if (heroDTO.getHero().getFaction() > 0 ){
            sql.append(" AND h.heroFaction = ?");
            params.add(heroDTO.getHero().getFaction());
        }
        if (heroDTO.getHero().getHeroStarLevel() > 0) {
            sql.append(" AND h.heroStarLevel = ?");
            params.add(heroDTO.getHero().getHeroStarLevel());
        }
        logger.info("实际执行sql语句: {}", sql.toString());
        logger.debug("实际输入参数: {}", params.toArray());
        return jdbcTemplate.query(sql.toString(), params.toArray(), new HeroRowMapper());
    }

    public void saveHeroes(List<Hero> heroes) {
        logger.info("开始批量插入武将数据");
        String sql = "INSERT INTO heroes (heroId, heroName, heroPic, heroStarLevel, enable) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, heroes, heroes.size(), (ps, hero) -> {
            ps.setInt(1, hero.getHeroId());
            ps.setString(2, hero.getHeroName());
            ps.setString(3, hero.getHeroPic());
            ps.setInt(4, hero.getHeroStarLevel());
            ps.setBoolean(5, hero.isEnable());
        });
    }
}
