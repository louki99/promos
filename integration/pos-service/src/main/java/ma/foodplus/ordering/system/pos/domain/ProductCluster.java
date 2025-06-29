package ma.foodplus.ordering.system.pos.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "product_cluster")
public class ProductCluster{

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column ( unique = true, nullable = false )
    private String code;

    @Column ( nullable = false )
    private String name;

    private String description;

    @OneToMany ( mappedBy = "productCluster" )
    private List<Product> products;

    public Long getId(){
        return id;
    }

    public String getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public List<Product> getProducts(){
        return products;
    }

    public void setCode(String code){
        this.code=code;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setProducts(List<Product> products){
        this.products=products;
    }
}