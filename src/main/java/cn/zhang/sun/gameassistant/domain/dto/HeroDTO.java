package cn.zhang.sun.gameassistant.domain.dto;

import cn.zhang.sun.gameassistant.domain.entity.Hero;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

public class HeroDTO extends BaseDTO {

    private static final long serialVersionUID = -1L;

    private Hero hero;

    private Query query;

    public HeroDTO() {
    }

    public HeroDTO(String JsonString) {
        HeroDTO heroDTO = getInstance(JsonString);
        if (heroDTO != null) {
            this.hero = heroDTO.getHero();
            this.query = heroDTO.getQuery();
        }
    }
    private HeroDTO getInstance(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return null;
        }
        //qeruyString的json字符串转化为heroDTO对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(queryString, HeroDTO.class);
        } catch (Exception e) {
            logger.error("HeroDTO的查询JsonString转换HeroDTO失败", e);
            return null;
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String toJsonString() {
        //将当前对象转化为json字符串
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            logger.error("HeroDTO对象转换JsonString失败", e);
            return null;
        }
    }
}
