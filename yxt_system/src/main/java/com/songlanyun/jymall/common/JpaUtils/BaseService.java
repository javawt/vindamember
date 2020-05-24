package com.songlanyun.jymall.common.JpaUtils;

import com.songlanyun.jymall.common.utils.PageUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

/**
 * Created by zenghang on 2019/11/21.
 */
public interface BaseService<T, ID> {
    /**
     * 查询所有记录
     *
     * @return
     */
    List<T> findAll();

    /**
     * 查询分页记录
     *
     * @param pageable
     * @return
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 查询排序记录
     *
     * @param sort
     * @return
     */
    List<T> findAll(Sort sort);

    /**
     * 根据Example为查询条件进行查询
     *
     * @param example
     * @return
     */
    List<T> findAll(Example<T> example);

    /**
     * 根据specification为查询条件进行查询
     *
     * @param specification
     * @return
     */
    List<T> findAll(Specification<T> specification);

    /**
     * 根据specification为查询条件进行查询
     *
     * @param params
     * @return
     */
    List<T> findAll(Map<String, Object> params);

    /**
     * 根据specification为查询条件进行查询并分页
     *
     * @param specification
     * @return
     */
    Page<T> findAll(Specification<T> specification, Pageable pageable);

    /**
     * 分页+单表复杂查询
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询单条记录
     */
    T findOne(Specification<T> specification);

    /**
     * 查询单条记录
     */
    T findOne(Map<String, Object> params);

    /**
     * 根据ID进行查询
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * 更新和保存
     *
     * @param t
     * @return
     */
    T save(T t);

    /**
     * 更新和保存所有集合
     *
     * @param ts
     * @return
     */
    List<T> saveAll(List<T> ts);

    /**
     * 删除所有数据
     */
    void deleteAll();

    /**
     * 根据实体类进行删除数据
     *
     * @param t
     */
    void delete(T t);

    /**
     * 根据主键ID为条件删除数据
     *
     * @param id
     */
    void deleteById(ID id);

    /**
     * 根据主键ID数组为条件删除数据
     *
     * @param ids
     */
    void deleteByIds(ID[] ids);

    /**
     * 统计所有条数
     *
     * @return
     */
    long count();

    /**
     * 根据查询条件获取条数
     *
     * @return
     */
    long count(Map<String, Object> param);

    /**
     * 根据查询条件获取条数
     *
     * @return
     */
    long count(Specification<T> specification);
}
