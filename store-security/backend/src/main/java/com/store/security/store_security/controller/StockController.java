package com.store.security.store_security.controller;

import com.store.security.store_security.dto.AllStockDto;
import com.store.security.store_security.dto.ArticleDto;
import com.store.security.store_security.dto.StockArticleDto;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.service.IStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Stock management",
        description = "REST API to manage stock"
)
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

    private final IStockService stockService;


    @Operation(
            summary = "Add article quantity",
            description = "Add article quantity"
    )
    @ApiResponse(
            responseCode = "200",
            description  = "HTTP Status 200 : OK"
    )
    @PatchMapping("/{id}/{quantity}")
    public ResponseEntity<StockArticleDto> addArticleQuantity(@PathVariable("id") Long id,
            @Min(value = 1,message = "Quantity for article invalid") @PathVariable("quantity") Integer quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.saveArticleQuantity(id, quantity));
    }

    @Operation(
            summary = "Decrement article quantity",
            description = "Decrement article quantity"
    )
    @ApiResponse(
            responseCode = "200",
            description  = "HTTP Status 200 : OK"
    )
    @PatchMapping("{id}/decrement/{quantity}")
    public ResponseEntity<StockArticleDto> decrementArticle(@PathVariable("id") Long id,
            @Min(value = 1,message = "Quantity for article invalid") @PathVariable("quantity") Integer quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.decrementArticle(id, quantity));
    }

    @Operation(
            summary = "Get all stock",
            description = "Get all stock"
    )
    @ApiResponse(
            responseCode = "200",
            description  = "HTTP Status 200 : OK"
    )
    @GetMapping
    public ResponseEntity<StockDto> getAllStock() {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getStock());
    }

    @Operation(
            summary = "Get stock by article",
            description = "Get stock by article"
    )
    @ApiResponse(
            responseCode = "200",
            description  = "HTTP Status 200 : OK"
    )
    @GetMapping("/{id}")
    public ResponseEntity<StockDto> getStockByArticle(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getStockByArticle(id));
    }

    @Operation(
            summary = "Add article",
            description = "Add article"
    )
    @ApiResponse(
            responseCode = "200",
            description  = "HTTP Status 200 : OK"
    )
    @PostMapping
    public ResponseEntity<ArticleDto> addArticle(@RequestBody @Valid ArticleDto articleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.loadArticle(articleDto));
    }


}
