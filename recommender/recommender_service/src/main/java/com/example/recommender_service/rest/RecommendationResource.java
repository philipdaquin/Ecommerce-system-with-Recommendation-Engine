package com.example.recommender_service.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.recommender_service.models.BestSeller;
import com.example.recommender_service.models.ProductViewResponse;
import com.example.recommender_service.service.BestSellerService;
import com.example.recommender_service.service.ProductService;
import com.example.recommender_service.service.ProductViewHistoryService;

@RestController
@RequestMapping("/api")
public class RecommendationResource {
    
    private final Logger log = LoggerFactory.getLogger(RecommendationResource.class);

    private ProductService productService;
    private BestSellerService bestSellerService;
    private ProductViewHistoryService productViewHistoryService;

    /**
     * 
     * @param userId
     * @return
     */
    @GetMapping("/get-product-recommendation/")
    public ResponseEntity<BestSeller> getlasProductViewResponse(@RequestParam(value = "userId") final Integer userId) { 
        log.debug("REST requuest to get product recommendations for {}", userId);
        return ResponseEntity
            .ok()
            .body(bestSellerService.getProductRecommendation(userId));
    }

    /**
     * @{code DELETE /delete-product-recommendation} : delete the product recommendations of "productId" from the user of "userId"
     * 
     * @param userId of the user 
     * @param productId of the proudct view history
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body Boolean
     */
    @DeleteMapping("/delete-product-recommendation")
    public ResponseEntity<Boolean> deleteProductViewHistory(
        @RequestParam(value = "userId") final Integer userId,
        @RequestParam(value = "productId") final Integer productId
    ) {
        log.debug("REST request to delete product view history for user: {userId}");
        return ResponseEntity
            .ok()
            .body(productViewHistoryService.deleteProductViewHistory(userId, productId));
    }


    /**
     * {@code GET /api/get-product-recommedation :userId} : get the "userId" of the user
     * @param userId the id of the user
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body the Recommended Products, or with 
     * status {@code 404 (Not Found)}
     */
    @GetMapping("/get-product-recommedation")
    public ResponseEntity<ProductViewResponse> getLastViewedProduct(@RequestParam(value = "userId") final Integer userId) { 
        List<Integer> productIds = productViewHistoryService.getLastViewedProduct(userId);
        
        ProductViewResponse result = new ProductViewResponse();
            result.setProductIds(productIds);
            result.setType("personalised");
            result.setUserId(userId);
        return ResponseEntity.ok().body(result);
    }


}
