
all jars need is in “lib” folder, FCL is in conf folder

Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!
Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!
Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!
Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!
Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!
Please keep “fuzzy.jar” “lib” and “conf” folder in the same directory!

==================How to use==============
—>CD to current directory.
—>java -jar fuzzy.jar args[0]:

Arguments:
	-t   change threshold of expecting goal Node (default is 80)
	-d   change depth of searching               (default is 4)
	-b   change breadth of expecting goal Node   (default is 2)

Settings should annouced before URL otherwise it will be regard as keywords!
You can use 'SFC' to see the fuzzy chart


For rich info website (which means keywords will appears a lot) 80 is a fair threshold.
Example:
->java -jar fuzzy.jar https://en.wikipedia.org/wiki/Disco nu disco


For weak info website (which means keywords may only appears in title, like a news) 40 is a fair threshold.
Example:
->java -jar fuzzy.jar -t 40 -d 5 -b 4 https://en.wikipedia.org/wiki/Disco nu funk

==========Disadvantage & Limitation==========

My assignment was testing using WIKIPEDIA web site, whose URLs may contains most keywords.
So if keywords appears in URL, this search engine will find it soon.
If not, it need luck to find the result:
When get the child nodes of a web page, we order them by frequency of keywords appears in its URL.
When add them to their parents node with a breadth limitation, a heuristic calculation will begin.
So if the best link doesn’t contain keywords in its link and its position in the queue is greater than the breadth limitation, it will be skipped.


====================Features==================

1. Ordering Nodes based on keywords frequency in URLs highly reduced the time of searching.

2. Fuzzy logic use 5 level of score(extreme low, low, medium, high, extreme high) ensure the website can be clearly levelled.

3. Flexibility for user to modify the threshold, depth and breadth of searching by passing arguments to the programme.

4. Thread pool have been used to control threads which generates children nodes of a node, the maximum amount is 50. Slightly reduce the overhead of the programme.

5. History library (A hash map) have been used to avoid visiting same node twice.

6. MANIFEST.MF have also set class path to the two jars in lib folder, so this program work independently.

7. Always give user a result. If no link above the threshold, programme will return the most possible link

8. Give back the track path of current link.
