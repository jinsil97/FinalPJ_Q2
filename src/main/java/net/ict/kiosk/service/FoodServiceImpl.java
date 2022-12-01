package net.ict.kiosk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.ict.kiosk.dto.FoodDTO;
import net.ict.kiosk.dto.RequestDTO;
import net.ict.kiosk.dto.ResponseDTO;
import net.ict.kiosk.entity.Food;
import net.ict.kiosk.repository.FoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class FoodServiceImpl implements FoodService{

    private final ModelMapper modelMapper;
    private final FoodRepository foodRepository;

//    @Override
//    public String register(FoodDTO foodDTO){
//        Food food = modelMapper.map(foodDTO, Food.class);
//        String foodName = foodRepository.save(food).getFoodName();
//        return foodName;
//    }

    @Override
    public String register(FoodDTO foodDTO){

        Food food = dtoToEntity(foodDTO);
        String foodName = foodRepository.save(food).getFoodName();

        return foodName;
    }

//    @Override
//    public FoodDTO readOne(String foodName){
//
//        Optional<Food> result = foodRepository.findById(foodName);
//        Food food = result.orElseThrow();
//        FoodDTO foodDTO = modelMapper.map(food,FoodDTO.class);
//        return foodDTO;
//    }

    @Override
    public FoodDTO readOne(String foodName){

        Optional<Food> result = foodRepository.findByIdWithImages(foodName);
        Food food = result.orElseThrow();
        FoodDTO foodDTO = entityToDTO(food);

        return foodDTO;
    }


    @Override
    public FoodDTO read(){

        List<Food> foods = foodRepository.findAll();
        FoodDTO foodDTO = modelMapper.map(foods,FoodDTO.class);
        return foodDTO;

    }

    @Override
    public void modify(FoodDTO foodDTO){

        Optional<Food> result = foodRepository.findById(foodDTO.getFoodName());
        Food food = result.orElseThrow();
        food.change(foodDTO.getCategory(),foodDTO.getDescription(),foodDTO.getPrice());

        //첨부파일의 처리
        food.clearImages();

        if(foodDTO.getFileNames() != null){
            for (String fileName : foodDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                food.addImage(arr[0], arr[1]);
            }
        }
        foodRepository.save(food);
    }

    @Override
    public void remove(String foodName){
        foodRepository.deleteById(foodName);
    }


    @Override
    public ResponseDTO<FoodDTO> list(RequestDTO requestDTO){

        String[] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();

        List<Food> result = foodRepository.search(types, keyword);

        List<FoodDTO> dtoList = result.stream().map(food -> modelMapper.map(food,FoodDTO.class))
                .collect(Collectors.toList());

        return ResponseDTO.<FoodDTO>withAll()
                .dtoList(dtoList)
                .build();

        }


    }



