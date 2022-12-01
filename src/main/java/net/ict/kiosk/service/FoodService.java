package net.ict.kiosk.service;

import net.ict.kiosk.dto.FoodDTO;
import net.ict.kiosk.dto.FoodListAllDTO;
import net.ict.kiosk.dto.RequestDTO;
import net.ict.kiosk.dto.ResponseDTO;
import net.ict.kiosk.entity.Food;

import java.util.List;
import java.util.stream.Collectors;

public interface FoodService {

    String register(FoodDTO foodDTO);

    FoodDTO readOne(String foodName);

    FoodDTO read();

    void modify(FoodDTO foodDTO);

    void remove(String foodName);

    ResponseDTO<FoodDTO> list(RequestDTO requestDTO);

  //  ResponseDTO<FoodListAllDTO> listWithAll(RequestDTO requestDTO);

    default Food dtoToEntity(FoodDTO foodDTO){

        Food food = Food.builder()
                .foodName(foodDTO.getFoodName())
                .category(foodDTO.getCategory())
                .description(foodDTO.getDescription())
                .price(foodDTO.getPrice())
                .build();

        if(foodDTO.getFileNames() != null){
            foodDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                food.addImage(arr[0], arr[1]);
            });
        }
        return food;
    }

    default FoodDTO entityToDTO(Food food){

        FoodDTO foodDTO = FoodDTO.builder()
                .foodName(food.getFoodName())
                .category(food.getCategory())
                .description(food.getDescription())
                .price(food.getPrice())
                .build();

        List<String> fileNames =
                food.getImageSet().stream().sorted().map(foodImage ->
                        foodImage.getUuid()+"_"+foodImage.getFileName()).collect(Collectors.toList());

        foodDTO.setFileNames(fileNames);

        return foodDTO;
    }


}
