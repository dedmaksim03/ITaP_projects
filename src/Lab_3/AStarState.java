package Lab_3;

import java.util.HashMap;
import java.util.Set;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    private HashMap<Location, Waypoint> openWP = new HashMap<>();
    private HashMap<Location, Waypoint> closeWP = new HashMap<>();


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     * Эта функция должна проверить все вершины в наборе открытых вершин, и после этого она должна вернуть
     * ссылку на вершину с наименьшей общей стоимостью. Если в "открытом" наборе нет вершин, функция возвращает NULL
     **/
    public Waypoint getMinOpenWaypoint()
    {
        Set<Location> keys = openWP.keySet(); // Список всех ключей
        float min = 0f; // Минимальное значение total cost
        Waypoint minWP = null; // Ссылка на пустой объект

        for (Location loc: keys){ // Перебираем каждый ключ в openWP
            if (min == 0f){ // Выполняется один раз для первой инициализации min
                min = openWP.get(loc).getTotalCost();
                minWP = openWP.get(loc);
            }
            else if (openWP.get(loc).getTotalCost() < min ){
                min = openWP.get(loc).getTotalCost();
                minWP = openWP.get(loc);
            }
        }
        return minWP;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     * Если в наборе «открытых вершин» в настоящее время нет вершины для данного местоположения,
     * то необходимо просто добавить новую вершину. Если в наборе «открытых вершин» уже есть вершина для этой локации,
     * добавьте новую вершину только в том случае, если стоимость пути до новой вершины меньше стоимости пути до текущей.
     * Пусть данный метод вернет значение true, если новая вершина была успешно добавлена в набор, и false в противном случае.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        if (!openWP.containsKey(newWP.getLocation())){ // Если вершины нет в списке (поиск по ключу), то
            openWP.put(newWP.getLocation(), newWP);    // добавляем новую пару
            return true;
        }
        else{  // Иначе сравниваем уже имеющуюся вершину с переданной
            Waypoint oldWP = openWP.get(newWP.getLocation()); // Имеющаяся вершина
            if (oldWP.getPreviousCost() > newWP.getPreviousCost()){ // Если стоимость имеющейся вершины выше, то
                openWP.put(newWP.getLocation(), newWP);             // Вставляем новую пару с новой вершиной
                return true;
            }
        }
        return false;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return openWP.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     * Эта функция перемещает вершину из набора «открытых вершин» в набор «закрытых вершин».
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint wp = openWP.get(loc); // Сохраняем значение вершины
        openWP.remove(loc); // Удаляем пару из открытых вершин
        closeWP.put(loc, wp); // Вносим эту пару в закрытые вершины
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     * Эта функция должна возвращать значение true, если указанное местоположение
     * встречается в наборе закрытых вершин, и false в противном случае.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closeWP.containsKey(loc);
    }
}

