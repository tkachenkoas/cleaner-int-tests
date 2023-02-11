package com.example.cleaner_int_tests.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

import static java.util.Arrays.asList;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String instructions;
    private String ingredients;

    @Transient
    public List<String> getIngredientsList() {
        return asList(ingredients.split(","));
    }

    @Transient
    public void setIngredientsList(List<String> ingredients) {
        this.ingredients = String.join(",", ingredients);
    }
}
