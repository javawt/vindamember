### Jpa基类使用方式
##### 1.创建实体层
```java
@Data
@Entity
@Table(name = "demo")
public class Demo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
```
* 关键注解    
<table border="1px" align="center" bordercolor="black" width="80%" height="100px">
    <tr align="center">
        <td>@Entity</td>
        <td>标注该类为一个实体  </td>
    </tr>
    <tr align="center">
        <td>@Table(name = "数据库表名")</td>
        <td>标注该实体对应着那个数据库</td>
    </tr>
    <tr align="center">
        <td>@Id</td>
        <td>主键ID注解</td>
    </tr>
    <tr align="center">
        <td>@GeneratedValue(strategy = GenerationType.IDENTITY)</td>
        <td>自增长</td>
    </tr>
    <tr align="center">
        <td>@Column(name = "数据库列名")</td>
        <td>该注解标注在字段上面</td>
    </tr>
</table>

> 注：不打`@Column`的注解数据库列名的创建模式为驼峰命名

#### 2.创建`Repository`层
```java
@Repository
public interface DemoRepository extends BaseRepository<Demo, Integer> {
}
```
> `BaseRepository` 中传入的两个类分别为对应的`实体类`和对应的实体类中的`主键ID的属性`    
> 注：不要忘了打上这个注解`@Repository`

#### 3.创建`Service`层
```java
public interface DemoService extends BaseService<Demo, Integer> {
}
```

#### 4.创建实现类
```java
@Service
public class DemoServiceImpl extends BaseServiceImpl<Demo, Integer, DemoRepository> implements DemoService {

    public DemoServiceImpl(DemoRepository repository) {
        super(repository);
    }
}
```
> 注意打上`@Service`注解
> 继承`BaseServiceImpl`传入的三个实体类分别为对应的`实体类`、实体类的`主键ID的属性`、`对应的Repository`
> 注意构造函数的写法，同时构造函数也是必须的

### 示例
* 查询所有列表数据
```java
List<Demo> list = demoService.findAll();
```
* 查询分页数据
```java
Pageable pageable = PageRequest.of(1, 10);
Page<Demo> page = demoService.findAll(pageable);
```
* 查询分页数据并排序
```java
Sort sort = new Sort(Sort.Direction.DESC, "id");
Pageable pageable = PageRequest.of(1, 10, sort);
Page<Demo> page = demoService.findAll(pageable);
```
* 单表复杂查询
```java
List<Demo> page = imgsService.findAll((Specification<Demo>) (root, query, criteriaBuilder) -> {

    List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.equal(root.get("id"), 1));
    predicates.add(criteriaBuilder.like(root.get("name"), "张"));
    predicates.add(criteriaBuilder.ge(root.get("age"), 18));

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
});
```
* 单表复杂查询+分页
```java
Pageable pageable = PageRequest.of(1, 10, sort);
Page<Demo> page = imgsService.findAll((Specification<Demo>) (root, query, criteriaBuilder) -> {

    List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.equal(root.get("id"), 1));
    predicates.add(criteriaBuilder.like(root.get("name"), "张"));
    predicates.add(criteriaBuilder.ge(root.get("age"), 18));

    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
}, pageable);
```
* 单表复杂查询+分页
> 基于人人的框架的`PageUtils`封装，实现方式和上面的Dome相同
```java
Map<String,Object> params = new HashMap<>();

//分页参数提前约定为page size
params.put("page", 1);
params.put("size", 10);

//key为字段名，value为具体查询值
//查询条件目前所有的都是等于，如果过度封装将对参数有命名格式有要求
params.put("id", 1);
params.put("name","张三");
params.put("age",18);

//排序，key为排序方式，value为具体字段名，多个字段名用英文逗号分隔
params.put("asc", "id,name");
params.put("desc", "age");

PageUtils page = imgsService.queryPage(params);
```
> 对于单表的复杂查询使用Specification时，可以基于单表复杂查询+分页的基础上进行查询
```java
List<Demo> list = imgsService.findAll(new SpecificationUtil<Imgs>().getSpecification(params));
```