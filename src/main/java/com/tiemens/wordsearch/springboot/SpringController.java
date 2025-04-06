package com.tiemens.wordsearch.springboot;


import com.tiemens.wordsearch.model.WordSearchModel;
import com.tiemens.wordsearch.modelio.WordSearchJson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SpringController {

    private SpringService springService;

    public SpringController(@Autowired SpringService ss) {
        this.springService = ss;
    }


    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public String init(@ModelAttribute("model") ModelMap model) {
        System.out.println("INIT /cars");
        List<String> carList = getCarlist();
        model.addAttribute("carList", carList);
        return "index2";
    }

    private List<String> getCarlist() {
        return new ArrayList<>();
    }
    /*
    @RequestMapping("/hello")
    public Map<String, Object> showHelloWorld() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld");
        return map;
    }
*/

    @GetMapping("/index2")
    public ModelAndView index2() {
        ModelAndView modelAndView = new ModelAndView("index2");
        modelAndView.addObject("title", "Freemarker");
        return modelAndView;
    }

    @GetMapping("/tim")
    public String tim() {
        return "This is a page";
    }


    @GetMapping("/tim2")
    public ModelAndView tim2() {
        System.out.println("ENTER tim2");
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping("/tim3JustIndex")
    public String index(Model model) {
        model.addAttribute("title", "This is tim3 model.title");
        System.out.println("ENTER tim3 model=" + model);
        return "index4";
    }

    @GetMapping("/tim3")
    public ModelAndView tim3(Model model) {
        ModelAndView modelAndView = new ModelAndView("index");
        model.addAttribute("title", "This is tim3 model.title");
        modelAndView.addObject("title", "This is addObject");
        System.out.println("ENTER tim3 model=" + model);
        return modelAndView;
    }

    @GetMapping("/session-id")
    public String getSessionId(HttpSession session) {
        return session.getId();
    }

    private Map<String, Map<String, WordSearchModel>> mapSessiontoMapFilenametoModel =
            new HashMap<>();
    private Map<String, WordSearchModel> mapFilename2Model =
            new HashMap<>();

    @GetMapping("/api/loadfilename")
    public WordSearchModel getWordSearchModel(@RequestParam(name = "filename", required = true) String filename) {
        System.out.println("ENTER /api/loadfilename, filename=" + filename);

        final String key = filename;
        WordSearchModel wordSearchModel = springService.getFromFile(filename);
        mapFilename2Model.put(filename, wordSearchModel);

        System.out.println("  EXIT /api/loadfilename, model=" + wordSearchModel);
        return wordSearchModel;
    }



    @GetMapping("/game")
    public ModelAndView gameDisplay(@RequestParam(name = "fileName", required = true) String fileName) {
        System.out.println("ENTER gameDisplay, shortName=" + fileName);

        WordSearchModel wsm = springService.getFromFile(fileName);
        if (wsm == null) {
            System.out.println("We failed to load filename=" + fileName);
            ModelAndView modelAndView = new ModelAndView("gameloaderror");
            modelAndView.addObject("fileName", fileName);
            return modelAndView;
        } else {
            System.out.println("  Loaded " + fileName + " with #rows=" + wsm.getRows());

            ModelAndView modelAndView = new ModelAndView("game");
            modelAndView.addObject("title", "This is addObject noargs");
            modelAndView.addObject("model", wsm);
            modelAndView.addObject("gameModel", wsm);
            System.out.println("  EXIT gameDisplay model=" + modelAndView);
            return modelAndView;
        }
    }

    @GetMapping("/")
    public ModelAndView index() {
        System.out.println("ENTER index");
        List<String> filenames = springService.getFilenamesFromDirectory("src/input");
        System.out.println("ENTER index, theList=" + filenames);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("listoffilenames", filenames);
        return modelAndView;
    }
}
