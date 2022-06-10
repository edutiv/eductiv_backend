// package com.projectlms.projectlms.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.projectlms.projectlms.domain.dto.UserDto;
// import com.projectlms.projectlms.service.UserService;

// @RestController
// @RequestMapping(value = "/user")
// public class UserController {
//     private final UserService userService;

//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     @PostMapping(value = "")
//     public ResponseEntity<Object> addUser(@RequestBody UserDto request) {
//         return userService.addUser(request);
//     }

//     @GetMapping(value = "")
//     public ResponseEntity<Object> getAllUser() {
//         return userService.getAllUser();
//     }

//     @GetMapping(value = "/detail/{id}")
//     public ResponseEntity<Object> getUserDetail(@PathVariable(value = "id") Long id) {
//         return userService.getUserDetail(id);
//     }

//     @DeleteMapping(value = "/{id}")
//     public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {
//         return userService.deleteUser(id);
//     }

//     @PutMapping(value = "/{id}")
//     public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id, @RequestBody UserDto request) {
//         return userService.updateUser(request, id);
//     }
// }
