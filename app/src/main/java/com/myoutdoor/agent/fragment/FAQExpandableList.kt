package com.myoutdoor.agent.fragment
import android.view.View
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import com.myoutdoor.agent.R
import com.myoutdoor.agent.adapter.ThreeLevelListAdapter
import com.myoutdoor.agent.utils.BaseFragment


class FAQExpandableList : BaseFragment() {

    var expandableListView: ExpandableListView? = null

    // We have two  main category. (third one is left for example addition)

    // We have two  main category. (third one is left for example addition)
    /**
     * The Parent Group Names.
     */
    var parent = arrayOf("MOVIES", "GAMES") // comment this when uncomment bottom

    //String[] parent = new String[]{"MOVIES", "GAMES", "SERIALS"}; // example for 3 main category lists

    /*
    If above line is uncommented uncomment the following too:
    - serials array
    - serials genre list
    - Datastructure for Third level Serials.
    - secondLevel.add(serials);
    - serials category all data
    - data.add(thirdLevelSerials);

     */

    //String[] parent = new String[]{"MOVIES", "GAMES", "SERIALS"}; // example for 3 main category lists
    /*
    If above line is uncommented uncomment the following too:
    - serials array
    - serials genre list
    - Datastructure for Third level Serials.
    - secondLevel.add(serials);
    - serials category all data
    - data.add(thirdLevelSerials);

     */
    /**
     * The Movies Genre List.
     */
    // We have two  main category. (third one is left for example addition)
    var movies = arrayOf("Horror", "Action", "Thriller/Drama")

    /**
     * The Games Genre List.
     */
    var games = arrayOf("Fps", "Moba", "Rpg", "Racing")

    /**
     * The Serials Genre List.
     */
    // String[] serials = new String[]{"Crime", "Family", "Comedy"};


    /**
     * The Serials Genre List.
     */
    // String[] serials = new String[]{"Crime", "Family", "Comedy"};
    /**
     * The Horror movie list.
     */
    // movies category has further genres
    var horror = arrayOf("Conjuring", "Insidious", "The Ring")

    /**
     * The Action Movies List.
     */
    var action = arrayOf("Jon Wick", "Die Hard", "Fast 7", "Avengers")

    /**
     * The Thriller Movies List.
     */
    var thriller = arrayOf(
        "Imitation Game",
        "Tinker, Tailer, Soldier, Spy",
        "Inception",
        "Manchester by the Sea"
    )


    /**
     * The Fps games.
     */
    // games category has further genres
    var fps =
        arrayOf("CS: GO", "Team Fortress 2", "Overwatch", "Battlefield 1", "Halo II", "Warframe")

    /**
     * The Moba games.
     */
    var moba = arrayOf("Dota 2", "League of Legends", "Smite", "Strife", "Heroes of the Storm")

    /**
     * The Rpg games.
     */
    var rpg = arrayOf(
        "Witcher III",
        "Skyrim",
        "Warcraft",
        "Mass Effect II",
        "Diablo",
        "Dark Souls",
        "Last of Us"
    )

    /**
     * The Racing games.
     */
    var racing = arrayOf("NFS: Most Wanted", "Forza Motorsport 3", "EA: F1 2016", "Project Cars")

    // serials genre list
    /*String[] crime = new String[]{"CSI: MIAMI", "X-Files", "True Detective", "Sherlock (BBC)", "Fargo", "Person of Interest"};

    String[] family = new String[]{"Andy Griffith", "Full House", "The Fresh Prince of Bel-Air", "Modern Family", "Friends"};

    String[] comedy = new String[]{"Family Guy", "Simpsons", "The Big Bang Theory", "The Office"};
*/


    // serials genre list
    /*String[] crime = new String[]{"CSI: MIAMI", "X-Files", "True Detective", "Sherlock (BBC)", "Fargo", "Person of Interest"};

    String[] family = new String[]{"Andy Griffith", "Full House", "The Fresh Prince of Bel-Air", "Modern Family", "Friends"};

    String[] comedy = new String[]{"Family Guy", "Simpsons", "The Big Bang Theory", "The Office"};
*/
    /**
     * Datastructure for Third level movies.
     */
    var thirdLevelMovies = LinkedHashMap<String, Array<String>>()

    /**
     * Datastructure for Third level games.
     */
    var thirdLevelGames = LinkedHashMap<String, Array<String>>()

    /**
     * Datastructure for Third level Serials.
     */
    // LinkedHashMap<String, String[]> thirdLevelSerials = new LinkedHashMap<>();


    /**
     * Datastructure for Third level Serials.
     */
    // LinkedHashMap<String, String[]> thirdLevelSerials = new LinkedHashMap<>();
    /**
     * The Second level.
     */
    var secondLevel: List<Array<String>>? = ArrayList()


    /**
     * The Data.
     */
    var data: List<LinkedHashMap<String, Array<String>>> = ArrayList()


    override fun getLayoutId(): Int {
        return R.layout.fragment_f_a_q_expandable_list
    }

    override fun onCreateView() {

        // second level category names (genres)


        // second level category names (genres)
//        secondLevel.add(movies)
//        secondLevel.add(games)
        // secondLevel.add(serials);

        // movies category all data
        // secondLevel.add(serials);

        // movies category all data
        thirdLevelMovies[movies[0]] = horror
        thirdLevelMovies[movies[1]] = action
        thirdLevelMovies[movies[2]] = thriller


        // games category all data


        // games category all data
        thirdLevelGames[games[0]] = fps
        thirdLevelGames[games[1]] = moba
        thirdLevelGames[games[2]] = rpg
        thirdLevelGames[games[3]] = racing


        // serials category all data
        /*  thirdLevelSerials.put(serials[0], crime);
        thirdLevelSerials.put(serials[1], family);
        thirdLevelSerials.put(serials[2], comedy);
*/


        // all data


        // serials category all data
        /*  thirdLevelSerials.put(serials[0], crime);
        thirdLevelSerials.put(serials[1], family);
        thirdLevelSerials.put(serials[2], comedy);
*/


        // all data
//        data.add(thirdLevelMovies)
//        data.add(thirdLevelGames)
//        //data.add(thirdLevelSerials);


        // expandable listview
        //data.add(thirdLevelSerials);


        // expandable listview
//        expandableListView = findViewById<View>(R.id.expandible_listview) as ExpandableListView

        // parent adapter

        // parent adapter
        val threeLevelListAdapterAdapter = ThreeLevelListAdapter(requireContext(), parent,
            secondLevel as MutableList<Array<String>>?, data as MutableList<LinkedHashMap<String, Array<String>>>
        )


        // set adapter


        // set adapter
        expandableListView!!.setAdapter(threeLevelListAdapterAdapter)


        // OPTIONAL : Show one list at a time


        // OPTIONAL : Show one list at a time
        expandableListView!!.setOnGroupExpandListener(object : OnGroupExpandListener {
            var previousGroup = -1
            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup) expandableListView!!.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })


    }

}