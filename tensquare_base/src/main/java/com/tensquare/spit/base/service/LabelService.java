package com.tensquare.spit.base.service;

import com.tensquare.spit.base.dao.LabelDao;
import com.tensquare.spit.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *  保存标签
     * @param label
     */
    public void save(Label label) {
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 根据id删除标签
     * @param labelId
     */
    public void deleteById(String labelId) {
        labelDao.deleteById(labelId);
    }


    /**
     * 根据id更新标签
     * @param labelId
     * @param label
     */
    public void updateById(String labelId, Label label) {
        label.setId(labelId);//以地址栏传回来的id为准
       labelDao.save(label);
    }

    /**
     * 查询所有标签
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据id查询标签
     * @param labelId
     * @return
     */
    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();//get方法获取单个对象
    }

    public List<Label> findByCondition(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            /**
             *
             * @param root 根对象，把条件封装哪个对象中？   相当于 where root.get("labelname").as(String.class) = "%"+label.getLabelname()+"%"
             * @param criteriaQuery 封装的都是查询关键字，如 groud by order y
             * @param cb   用来封装条件对象的，如果直接返回null，则表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new 一个集合，存放所有条件
                List<Predicate> predicateList = new ArrayList<>();
                if(label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");//相当于where labelname like "%小白%"
                    predicateList.add(predicate);
                }
                if(label.getState()!=null && !"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());//相当于where state = "1"
                    predicateList.add(predicate);
                }
                //new一个Predicate数组 ，最终返回值需要可变参数，所以要一个数组
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                //把list集合转换成arr数组
                predicateArr = predicateList.toArray(predicateArr);
                return cb.and(predicateArr); // 连接条件 相当于 where labelname like "%小白%" and  state = "1"
            }
        });
    }

    /**
     * 根据条件查询分页
     * @param label
     * @return
     */
    public Page<Label> findPageByCondition(Label label,int page,int size) {
        Pageable pageable = PageRequest.of(page-1,size);//我们传回来的是>=1,框架里面的是》=0,所以我们要按照框架的规则进行改值
        return labelDao.findAll(new Specification<Label>() {
            /**
             *
             * @param root 根对象，把条件封装哪个对象中？   相当于 where root.get("labelname").as(String.class) = "%"+label.getLabelname()+"%"
             * @param criteriaQuery 封装的都是查询关键字，如 groud by order y
             * @param cb   用来封装条件对象的，如果直接返回null，则表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new 一个集合，存放所有条件
                List<Predicate> predicateList = new ArrayList<>();
                if(label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");//相当于where labelname like "%小白%"
                    predicateList.add(predicate);
                }
                if(label.getState()!=null && !"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());//相当于where state = "1"
                    predicateList.add(predicate);
                }
                //new一个Predicate数组 ，最终返回值需要可变参数，所以要一个数组
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                //把list集合转换成arr数组
                predicateArr = predicateList.toArray(predicateArr);
                return cb.and(predicateArr); // 连接条件 相当于 where labelname like "%小白%" and  state = "1"
            }
        }, pageable);
    }
}
