package com.pat.service.impl;

import com.pat.mapper.CarouselMapper;
import com.pat.pojo.Carousel;
import com.pat.service.CarouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/10 1:02 下午
 * @Modify
 */
@Service
public class CarouseServiceImpl implements CarouseService {

    @Autowired
    CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        List<Carousel> result = carouselMapper.selectByExample(example);

        return result;
    }

}
