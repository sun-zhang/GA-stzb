package cn.zhang.sun.gameassistant.controller;

import cn.zhang.sun.gameassistant.common.enums.SystemErrors;
import cn.zhang.sun.gameassistant.domain.dto.BaseDTO;
import cn.zhang.sun.gameassistant.domain.dto.ErrorDTO;
import cn.zhang.sun.gameassistant.domain.dto.HeroDTO;
import cn.zhang.sun.gameassistant.service.HeroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hero")
public class HeroController {

    private static final Logger logger = LoggerFactory.getLogger(HeroController.class);

    @Autowired
    HeroService heroService;

    @RequestMapping("/getHero")
    public String getHero() {
        return "Hello, Hero!";
    }

    /**
     * 获取武将列表
     * @param queryString 查询条件
     * @return 基于BaseDTO的返回结果;错误信息返回ErrorDTO
     */
//    @RequestMapping("/getHeroes")
    @Deprecated
    public BaseDTO getHeroes3(@RequestParam("qs") String queryString) {
        //做基本的校验
        if (StringUtils.isEmpty(queryString)) {
            logger.warn("HeroDTO的查询JsonString为空");
            return new ErrorDTO(SystemErrors.DATA_NOT_VALID);
        }
        HeroDTO heroDTO = null;
        //qeruyString的json字符串转化为heroDTO对象
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            heroDTO = objectMapper.readValue(queryString, HeroDTO.class);
        } catch (Exception e) {
            logger.error("HeroDTO的查询JsonString转换HeroDTO失败", e);
        }
        if (heroDTO == null) {
            logger.warn("HeroDTO的查询JsonString转换HeroDTO失败");
            return new ErrorDTO(SystemErrors.DATA_NOT_VALID);
        }
        return heroService.getHeroList(heroDTO);
    }

    @RequestMapping("/getHeroes")
    public BaseDTO getHeroes(@RequestBody HeroDTO heroDTO) {
        if (heroDTO == null) {
            logger.warn("HeroDTO对象为空，确认是否传入了正确的参数");
            return new ErrorDTO(SystemErrors.DATA_NOT_VALID);
        }
        logger.debug("接收到的参数：{}", heroDTO.toJsonString());
        return heroService.getHeroList(heroDTO);
    }
}
