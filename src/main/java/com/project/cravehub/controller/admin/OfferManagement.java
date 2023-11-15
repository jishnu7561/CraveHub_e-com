package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.OfferDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.CategoryOffer;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.ProductOffer;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.CategoryOfferRepository;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductOfferRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.service.categoryservice.CategoryService;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class OfferManagement {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryOfferRepository categoryOfferRepository;

    @GetMapping("/offers")
    public String showOfferPage()
    {
        return "offerManagement";
    }
    @GetMapping("/listProductOffers")
    public String listProductOffersGet(HttpSession session, Model model)
    {
        model.addAttribute("productOfferSuccess",(String)session.getAttribute("productOfferSuccess"));
        model.addAttribute("productOfferError",(String)session.getAttribute("productOfferError"));
        session.removeAttribute("productOfferSuccess");
        session.removeAttribute("productOfferError");
        List<ProductOffer> productOfferList = productOfferRepository.findAll();
        model.addAttribute("productOfferList",productOfferList);
        return "listProductOffers";
    }
    @GetMapping("/addProductOffer")
    public String addProductOfferGet()
    {
        return "addProductOffer";
    }

    @PostMapping("/addProductOffer")
    public String addProductOfferPost(@ModelAttribute("productOffer")OfferDto offerDto,
                                      HttpSession session) {
        System.out.println("addProductOfferPost Method Called...... ");
        ProductOffer saved = productService .saveProductOffer(offerDto);
        if (saved != null) {
            session.setAttribute("productOfferSuccess","product offer successfully added.");
            System.out.println("product offer successfully saved----- "+ saved);
            return "redirect:/admin/listProductOffers";
        }
        session.setAttribute("productOfferError","failed to add product offer");
        return "redirect:/admin/listProductOffers";
    }

    @GetMapping("/manageProducts/{productOfferId}")
    public String manageProducts(@PathVariable("productOfferId") Integer id,Model model,
                                 @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm,
                                 HttpSession session){
        if(searchTerm != null)
        {
            List<Product> productList = productService.getProductsByPartialProductName(searchTerm);
            model.addAttribute("productList", productList);
        }
        else {
            List<Product> productList = productRepository.findAll();
            model.addAttribute("productList", productList);
        }
        model.addAttribute("offerId", id);
        return "manageProductsOffer";
    }


    @PostMapping("/addProductToOffer")
    @ResponseBody
    public Map<String,String> addProductToOffer(@RequestParam("productId") Integer productId,
                                                 @RequestParam("offerId") Integer offerId,
                                                 HttpSession session)
    {
        String added = productService.addProductToOffer(productId,offerId);
        System.out.println("the product id is "+ productId);
        System.out.println("the offerId is "+offerId);
        Map<String,String> response = new HashMap<>();
        if(added.equals("exist"))
        {
            response.put("isValid","false");
            response.put("message","product already exist");
        } else if (added.equals("success")) {
            response.put("isValid","true");
        }else {
            response.put("isValid","false");
            response.put("message","Failed to add product");
        }
        return response;
    }

    @PostMapping("/removeProductFromOffer")
    public String removeProductFromOffer(@RequestParam("offerId") Integer offerId,
                                         @RequestParam("productId") Integer productId,
                                         HttpSession session) {
        boolean removed = productService.removeProductFromOffer(offerId,productId);
        if(removed)
        {
            session.setAttribute("productOfferSuccess","product removed successfully");
        }else{
            session.setAttribute("productOfferError","Failed to remove product");
        }
        return "redirect:/admin/listProductOffers";
    }

    @GetMapping ("/inactiveProductOffer/{id}")
    public String inactiveProductOffer(@PathVariable Integer id)
    {
        productService.inactiveProductOffer(id);
        return "redirect:/admin/listProductOffers";

    }

    @GetMapping("/activeProductOffer/{id}")
    public String activeProductOffer(@PathVariable Integer id)
    {
        productService.activeProductOffer(id);

        return "redirect:/admin/listProductOffers";
    }

    @GetMapping("/editProductOffer/{id}")
    public String editProductOfferGet(@PathVariable("id") Integer id,Model model)
    {
        Optional<ProductOffer> productOfferOptional = productOfferRepository.findById(id);
        model.addAttribute("productOfferId",id);
        if(productOfferOptional.isPresent()) {
            model.addAttribute("productOffer",productOfferOptional.get());
        }
        else{
            model.addAttribute("error","something went wrong !!");
        }
        return "editProductOffer";
    }

    @PostMapping("/editProductOffer/{id}")
    public String editProductOfferPost(@PathVariable("id") Integer id,@ModelAttribute("productOffer") OfferDto offerDto,
                                       HttpSession session) {
        boolean edit = productService.editProductOfferById(id,offerDto);
        if(edit)
        {
            session.setAttribute("productOfferSuccess","ProductOffer details edited successfully ");

            return "redirect:/admin/listProductOffers";
        }
        session.setAttribute("productOfferError","failed to edit product offer details");

        return "redirect:/admin/listProductOffers";

    }


//    @GetMapping("/listCategoryOffers")
//    public String listCategoryOffersGet()
//    {
//        Optional<ProductOffer> productOffer = productOfferRepository.findById(1);
//        Set<Product> productList = productOffer.get().getProductList();
//        for(Product product : productList)
//        {
//            System.out.println(product.getProductName());
//        }
//        return "listCategoryOffers";
//    }

//    ################################## category ####################################

    @GetMapping("/listCategoryOffers")
    public String listCategoryOffersGet(HttpSession session, Model model)
    {
        model.addAttribute("categoryOfferSuccess",(String)session.getAttribute("categoryOfferSuccess"));
        model.addAttribute("categoryOfferError",(String)session.getAttribute("categoryOfferError"));
        session.removeAttribute("categoryOfferSuccess");
        session.removeAttribute("categoryOfferError");
        List<CategoryOffer> categoryOfferList = categoryOfferRepository.findAll();
        model.addAttribute("categoryOfferList",categoryOfferList);
        return "listCategoryOffers";
    }
    @GetMapping("/addCategoryOffer")
    public String addCategoryOfferGet()
    {
        return "addCategoryOffer";
    }

    @PostMapping("/addCategoryOffer")
    public String addCategoryOfferPost(@ModelAttribute("categoryOffer")OfferDto offerDto,
                                      HttpSession session) {
        System.out.println("addCategoryOfferPost Method Called...... ");
        CategoryOffer saved = categoryService .saveCategoryOffer(offerDto);
        if (saved != null) {
            session.setAttribute("categoryOfferSuccess","category offer successfully added.");
            System.out.println("category offer successfully saved----- "+ saved);
            return "redirect:/admin/listCategoryOffers";
        }
        session.setAttribute("categoryOfferError","failed to add category offer");
        return "redirect:/admin/listCategoryOffers";
    }

    @GetMapping("/manageCategories/{categoryOfferId}")
    public String manageCategory(@PathVariable("categoryOfferId") Integer id,Model model,
                                 @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm,
                                 HttpSession session){
        if(searchTerm != null)
        {
            List<Category> categoryList = categoryService.getCategoriesByPartialCategory(searchTerm);
            model.addAttribute("categoryList", categoryList);
        }
        else {
            List<Category> categoryList = categoryRepository.findAll();
            model.addAttribute("categoryList", categoryList);
        }
        model.addAttribute("offerId", id);
        return "manageCategoryOffer";
    }

    @PostMapping("/addCategoryToOffer")
    @ResponseBody
    public Map<String,String> addCategoryToOffer(@RequestParam("categoryId") Integer categoryId,
                                                @RequestParam("offerId") Integer offerId,
                                                HttpSession session)
    {
        String added = categoryService.addCategoryToOffer(categoryId,offerId);
        System.out.println("the product id is "+ categoryId);
        System.out.println("the offerId is "+offerId);
        Map<String,String> response = new HashMap<>();
        if(added.equals("exist"))
        {
            response.put("isValid","false");
            response.put("message","category already exist");
        } else if (added.equals("success")) {
            response.put("isValid","true");
        }else {
            response.put("isValid","false");
            response.put("message","Failed to add category");
        }
        return response;
    }

    @PostMapping("/removeCategoryFromOffer")
    public String removeCategoryFromOffer(@RequestParam("offerId") Integer offerId,
                                         @RequestParam("categoryId") Integer categoryId,
                                         HttpSession session) {
        boolean removed = categoryService.removeCategoryFromOffer(offerId,categoryId);
        if(removed)
        {
            session.setAttribute("categoryOfferSuccess","category removed successfully");
        }else{
            session.setAttribute("categoryOfferError","Failed to remove category");
        }
        return "redirect:/admin/listCategoryOffers";
    }

    @GetMapping ("/inactiveCategoryOffer/{id}")
    public String inactiveCategoryOffer(@PathVariable Integer id)
    {
        categoryService.inactiveCategoryOffer(id);
        return "redirect:/admin/listCategoryOffers";

    }

    @GetMapping("/activeCategoryOffer/{id}")
    public String activeCategoryOffer(@PathVariable Integer id)
    {
        categoryService.activeCategoryOffer(id);

        return "redirect:/admin/listCategoryOffers";
    }

    @GetMapping("/editCategoryOffer/{id}")
    public String editCategoryOfferGet(@PathVariable("id") Integer id,Model model)
    {
        Optional<CategoryOffer> categoryOfferOptional = categoryOfferRepository.findById(id);
        model.addAttribute("categoryOfferId",id);
        if(categoryOfferOptional.isPresent()) {
            model.addAttribute("categoryOffer",categoryOfferOptional.get());
        }
        else{
            model.addAttribute("error","something went wrong !!");
        }
        return "editCategoryOffer";
    }

    @PostMapping("/editCategoryOffer/{id}")
    public String editCategoryOfferPost(@PathVariable("id") Integer id,@ModelAttribute("productOffer") OfferDto offerDto,
                                       HttpSession session) {
        boolean edit = categoryService.editCategoryOfferById(id,offerDto);
        if(edit)
        {
            session.setAttribute("categoryOfferSuccess","CategoryOffer details edited successfully ");

            return "redirect:/admin/listCategoryOffers";
        }
        session.setAttribute("categoryOfferError","failed to edit category offer details");

        return "redirect:/admin/listCategoryOffers";

    }

}
