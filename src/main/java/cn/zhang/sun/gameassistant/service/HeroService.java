package cn.zhang.sun.gameassistant.service;

import cn.zhang.sun.gameassistant.domain.dao.HeroDao;
import cn.zhang.sun.gameassistant.domain.dto.HeroDTO;
import cn.zhang.sun.gameassistant.domain.dto.Query;
import cn.zhang.sun.gameassistant.domain.entity.Hero;
import cn.zhang.sun.gameassistant.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroService {

    private static Logger logger = LoggerFactory.getLogger(HeroService.class);

    @Autowired
    HeroDao heroDao;

    public HeroDTO getHeroList(HeroDTO heroDTO) {
        List<Hero> heroes = heroDao.getHeroes(heroDTO);
        if (heroes == null || heroes.size() == 0) {
            logger.warn("未查询到武将数据");
            return heroDTO;
        }
        if(heroDTO.getQuery()==null){
            heroDTO.setQuery(new Query());
        }
        heroDTO.getQuery().setTotalRecordNum(heroes.size());
        heroDTO.getQuery().setQueryResultJson(JsonUtil.convertListToJson(heroes));
        logger.debug("查询返回结果：{}",heroDTO.getQuery().getQueryResultJson());
        return heroDTO;
    }
}
