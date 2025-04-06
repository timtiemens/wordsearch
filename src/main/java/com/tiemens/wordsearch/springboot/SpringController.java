package com.tiemens.wordsearch.springboot;


import com.tiemens.wordsearch.model.WordSearchModel;
import com.tiemens.wordsearch.modelio.WordSearchJson;
import jakarta.servlet.http.HttpSession;
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
    public ModelAndView index() {
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
        WordSearchModel wordSearchModel = getFromFile(filename);
        mapFilename2Model.put(filename, wordSearchModel);

        System.out.println("  EXIT /api/loadfilename, model=" + wordSearchModel);
        return wordSearchModel;
    }

    private WordSearchModel getFromFile(String shortName) {
        String dirName = "src/input";
        String filename = dirName + "/" + shortName;
        try {
            return WordSearchJson.fromFile(filename).asWordSearchModel();
        } catch (IOException e) {
            System.out.println("Threw IO exception on " + filename + " : " + e);
            return null;
        }
    }

    @GetMapping("/game")
    public ModelAndView gameDisplay(@RequestParam(name = "fileName", required = true) String fileName) {
        System.out.println("ENTER gameDisplay, shortName=" + fileName);

        WordSearchModel wsm = getFromFile(fileName);
        if (wsm == null) {
            System.out.println("We failed to load filename=" + fileName);
            ModelAndView modelAndView = new ModelAndView("gameloaderror");
            modelAndView.addObject("fileName", fileName);
            return new ModelAndView("gameloaderror");
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


    public List<Item> getItemList() {
        return List.of(new Item("abba"),
                       new Item("baker"),
                       new Item("charlie"));
    }

    public static class Item {
        private final String name;

        public Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
