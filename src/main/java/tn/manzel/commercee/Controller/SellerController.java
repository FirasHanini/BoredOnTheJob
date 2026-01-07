package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;

import tn.manzel.commercee.Service.SellerService.SellerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

}
