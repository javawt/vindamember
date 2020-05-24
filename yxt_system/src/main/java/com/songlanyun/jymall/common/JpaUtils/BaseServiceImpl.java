package com.songlanyun.jymall.common.JpaUtils;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zenghang on 2019/11/21.
 */
public abstract class BaseServiceImpl<T, ID extends Serializable, Repository extends BaseRepository<T, ID>> implements BaseService<T, ID> {
    protected Repository repository;
    private static final String PAGE = "page";
    private static final String SIZE = "size";

    public BaseServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    @Override
    public List<T> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    @Override
    public List<T> findAll(Example<T> example) {
        return this.repository.findAll(example);
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return this.repository.findAll(specification);
    }

    @Override
    public List<T> findAll(Map<String, Object> params) {
        return this.findAll(new SpecificationUtil<T>().getSpecification(params));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        if (params.containsKey(PAGE) && params.containsKey(SIZE)) {
            int page = Integer.parseInt(String.valueOf(params.get(PAGE)));
            int size = Integer.parseInt(String.valueOf(params.get(SIZE)));

            params.remove(PAGE);
            params.remove(SIZE);
            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<T> pageData = this.findAll(new SpecificationUtil<T>().getSpecification(params), pageable);
                return new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), size, page);
            } catch (InvalidDataAccessApiUsageException ex) {
                throw new RRException("异常查询条件");
            }
        }
        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    @Override
    public T findOne(Specification<T> specification) {
        Optional<T> optional = this.repository.findOne(specification);
        return optional.orElse(null);
    }

    @Override
    public T findOne(Map<String, Object> params) {
        return this.findOne(new SpecificationUtil<T>().getSpecification(params));
    }

    @Override
    public T findById(ID id) {
        Optional<T> optional = this.repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public T save(T t) {
        return this.repository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> list) {
        return this.repository.saveAll(list);
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
    }

    @Override
    public void delete(T t) {
        this.repository.delete(t);
    }

    @Override
    public void deleteById(ID id) {
        this.repository.deleteById(id);
    }

    @Override
    public void deleteByIds(ID[] ids) {
        for (ID id : ids) {
            this.deleteById(id);
        }
    }

    @Override
    public long count() {
        return this.repository.count();
    }

    @Override
    public long count(Map<String, Object> param) {
        return this.count(new SpecificationUtil<T>().getSpecification(param));
    }

    @Override
    public long count(Specification<T> specification) {
        return this.repository.count(specification);
    }
}
