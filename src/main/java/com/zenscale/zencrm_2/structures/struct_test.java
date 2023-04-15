package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;


import javax.validation.constraints.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class struct_test {

    private String date;

    @NotBlank(message = "Please enter name")
    @Size(min = 4, message = "Name should be atleast 4 characters")
    @Size(max = 10, message = "Name should not be greater than 10 characters")
    private String name;

    @NotNull(message = "Please enter salary")
    @Min(value = 1000, message = "Salary must be atleast 1000.00")
    @Max(value = 10000, message = "Salary should not be greater than 10000.00")
    private Double salary;

    @Email(message = "Please enter valid email", regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
    @NotNull(message = "Please enter email")
    private String email;

    private int stageId;






}
