var complexityOutput;
var colorCounter = 1;
var clickedRow;
var superTable;
var pieparts;
var histoExists = false;
var pieExists = false;
var legendExists = false;
var tempVariableHolder;
$(document).on('ready', function(){
	$.ajax({
		context: this,
		dataType : "html",
		url : "navbar.htm",
		success : function(results) {
			$('#navholder').html(results);
		}
	});
	updateActiveListener();
});
$(document).on('click', '#ComplexityRunner', function () {
	var fileName = "MusicXMLs/" + getRealMusicPieceName() + ".xml";
	//Need some code here to execute the jar file with the specified parameters.
	$.ajax({
		type : "POST",
		url : "backend.php",
		data : {xmlName:fileName},
		success : function(results) {
			complexityOutput = $.parseJSON(results);
			//$('#noResultsTemp').html('');
			//$('#resultholder').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="retrievedData"></table>' );

			if (pieExists) {
				d3.select("#pieholder").select("svg").remove();
				pieExists = false;
			}
			if (legendExists) {
				d3.select("#pieholder").select("table").remove();
				legendExists = false;
			}

	    	if (histoExists) {
	    		d3.select("#dashboardholder").select("svg").remove();
	    		histoExists = false;
	    	}

			if (superTable != undefined) {
				superTable.clear();
			}
			else {
				superTable = $('#retrievedData').DataTable({
			        "columns": [
			            { "title": "Instrument Name" },
			            { "title": "Score" },
			            { "title": "Toughest Measure Number" },
			            { "title": "Toughest Measure Score" },
			            { "title": "Note Score" },
			            { "title": "Interval Score" }
			        ]
			    } );
			}

			var tempNames = [];
			var namesAndScores = [];
			var i = 0;
		    for (; i < complexityOutput.length; i++) {
		    	var item = complexityOutput[i];
		    	superTable.row.add( [
		            item.partName,
		            item.overallScore,
		            item.worstMeasureNumber,
		            item.worstMeasureValue,
		            item.noteTotal,
		            item.intervalTotal
		        ] ).draw();
		        if (tempNames.indexOf(item.partName) == -1) {
		        	namesAndScores.push({partName:item.partName,total:Math.floor(item.overallScore)});
		        	tempNames.push(item.partName);
		        }
		    }

		    if (i > 1) {
		    	dashboard("#dashboardholder", namesAndScores, true, false, false);
			}

			//$('#resultholder').text(results);
		},
		error : function(something) {
			alert("There was a problem. Please try again or contact the Music Scoring team.");
		}
	});
});

$(document).on('click', '#PDFVersion', function () {
	var fileName = "MusicPDFs/" + getRealMusicPieceName() + ".pdf";
	window.location.href = fileName;
});

$(document).on('click', '#retrievedData tbody tr', function () {
	clickedRow = $(this);
	var noteVal = Math.floor(Number(clickedRow[0].children[4].textContent));
	var intervalVal = Math.floor(Number(clickedRow[0].children[5].textContent));
	pieparts = [{type:"Notes",total:noteVal}, {type:"Intervals",total:intervalVal}];
	if (pieExists) {
		d3.select("#pieholder").select("svg").remove();
		pieExists = false;
	}
	if (legendExists) {
		d3.select("#pieholder").select("table").remove();
		legendExists = false;
	}
	dashboard("#pieholder", pieparts, false, true, true);
} );

$(function() {
	$(".musicpiece").click(function() {
    	// remove classes from all
    	$(".musicpiece").removeClass("active");
    	// add class to the one we clicked
    	$(this).addClass("active");
    	updateActiveListener();
	});
});

function updateActiveListener() {
	$("#selectedholder").text(getActiveMusicPiece());				
}

function getActiveMusicPiece() {
	return $(".musicpiece.active").children()[0].text;
}

function getRealMusicPieceName() {
	var htmlName = getActiveMusicPiece();
	var toReturn = "";
	switch(htmlName) {
		case 'The Hobbit for Clarinet' :
			toReturn += "The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet";
			break;
		case 'C Major Scale' :
			toReturn += "CMajorKey";
			break;
		case 'Actor Prelude' :
			toReturn += "ActorPreludeSample";
			break;
		default :
			toReturn += "The_Hobbit_The_Desolation_of_Smaug_Medley_for_Solo_Clarinet";
			break;
	}

	return toReturn;
}

function getAColor() {
	var colors = ["#4B0082", "#0000CD", "#008000", "#FFFF00", "#FFA500", "#FF0000", "#808080"];
	return colors[colorCounter++ % colors.length];
}

function dashboard(id, fData, histo, pie, legendBool){
    var barColor = 'steelblue';

    var namesAndColors = {Notes:getAColor(), Intervals:getAColor()};
    //function segColor(c){ return {Notes:"#807dba", Intervals:"#e08214",high:"#41ab5d"}[c]; }
    function matchColor(name){ return namesAndColors[name]; }
    
    // compute total for each state.
    //fData.forEach(function(d){d.total=d.freq.low+d.freq.mid+d.freq.high;});
    
    // function to handle histogram.
    function histoGram(fD){
        var hG={},    hGDim = {t: 60, r: 0, b: 80, l: 0};
        hGDim.w = 800 - hGDim.l - hGDim.r, 
        hGDim.h = 500 - hGDim.t - hGDim.b;
            
        //create svg for histogram.
        var hGsvg = d3.select(id).append("svg")
            .attr("width", hGDim.w + hGDim.l + hGDim.r)
            .attr("height", hGDim.h + hGDim.t + hGDim.b).append("g")
            .attr("transform", "translate(" + hGDim.l + "," + hGDim.t + ")");

        // create function for x-axis mapping.
        var x = d3.scale.ordinal().rangeRoundBands([0, hGDim.w], 0.1)
                .domain(fD.map(function(d) { return d[0]; }));

        // Add x-axis to the histogram svg.
        hGsvg.append("g").attr("class", "x axis")
            .attr("transform", "translate(0," + hGDim.h + ")")
            .call(d3.svg.axis().scale(x).orient("bottom"));

        // Create function for y-axis map.
        var y = d3.scale.linear().range([hGDim.h, 0])
                .domain([0, d3.max(fD, function(d) { return d[1]; })]);

        // Create bars for histogram to contain rectangles and freq labels.
        var bars = hGsvg.selectAll(".bar").data(fD).enter()
                .append("g").attr("class", "bar");
        
        //create the rectangles.
        bars.append("rect")
            .attr("x", function(d) { return x(d[0]); })
            .attr("y", function(d) { return y(d[1]); })
            .attr("width", x.rangeBand())
            .attr("height", function(d) { return hGDim.h - y(d[1]); })
            .attr('fill',getAColor());
            //.on("mouseover",mouseover)// mouseover is defined below.
            //.on("mouseout",mouseout);// mouseout is defined below.
            

        //Create the slanted x text
        hGsvg.attr("class", "x axis")
        	.selectAll("text")  
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
            });

        //Create the frequency labels above the rectangles.
        bars.append("text").text(function(d){ return d3.format(",")(d[1])})
            .attr("x", function(d) { return x(d[0])+x.rangeBand()/2; })
            .attr("y", function(d) { return y(d[1])-5; })
            .attr("text-anchor", "middle");
        
        /*
        function mouseover(d){  // utility function to be called on mouseover.
            // filter for selected state.
            var st = fData.filter(function(s){ return s.State == d[0];})[0],
                nD = d3.keys(st.freq).map(function(s){ return {type:s, freq:st.freq[s]};});
               
            // call update functions of pie-chart and legend.    
            pC.update(nD);
            leg.update(nD);
        }
        
        function mouseout(d){    // utility function to be called on mouseout.
            // reset the pie-chart and legend.    
            pC.update(tF);
            leg.update(tF);
        }
        
        // create function to update the bars. This will be used by pie-chart.
        hG.update = function(nD, color){
            // update the domain of the y-axis map to reflect change in frequencies.
            y.domain([0, d3.max(nD, function(d) { return d[1]; })]);
            
            // Attach the new data to the bars.
            var bars = hGsvg.selectAll(".bar").data(nD);
            
            // transition the height and color of rectangles.
            bars.select("rect").transition().duration(500)
                .attr("y", function(d) {return y(d[1]); })
                .attr("height", function(d) { return hGDim.h - y(d[1]); })
                .attr("fill", color);

            // transition the frequency labels location and change value.
            bars.select("text").transition().duration(500)
                .text(function(d){ return d3.format(",")(d[1])})
                .attr("y", function(d) {return y(d[1])-5; });            
        }    
        */    
        histoExists = true;
        return hG;
    }
    
    
    // function to handle pieChart.
    function pieChart(pD){
        var pC ={},    pieDim ={w:250, h: 250};
        pieDim.r = Math.min(pieDim.w, pieDim.h) / 2;
                
        // create svg for pie chart.
        var piesvg = d3.select(id).append("svg")
            .attr("width", pieDim.w).attr("height", pieDim.h).append("g")
            .attr("transform", "translate("+pieDim.w/2+","+pieDim.h/2+")");
        
        // create function to draw the arcs of the pie slices.
        var arc = d3.svg.arc().outerRadius(pieDim.r - 10).innerRadius(0);

        // create a function to compute the pie slice angles.
        var pie = d3.layout.pie().sort(null).value(function(d) { return d.total; });

        // Draw the pie slices.
        piesvg.selectAll("path").data(pie(pD)).enter().append("path").attr("d", arc)
            .each(function(d) { this._current = d; })
            .style("fill", function(d) {
            	//var tempCol = getAColor();
            	//namesAndColors[d.type] = tempCol;
            	//return tempCol;
            	//tempVariableHolder = d;
            	//alert("hold");
            	return matchColor(d.data.type);
            });
            //.on("mouseover",mouseover).on("mouseout",mouseout);

        // create function to update pie-chart. This will be used by histogram.
        pC.update = function(nD){
            piesvg.selectAll("path").data(pie(nD)).transition().duration(500)
                .attrTween("d", arcTween);
        }   

        /*     
        // Utility function to be called on mouseover a pie slice.
        function mouseover(d){
            // call the update function of histogram with new data.
            hG.update(fData.map(function(v){ 
                return [v.State,v.freq[d.data.type]];}),segColor(d.data.type));
        }
        //Utility function to be called on mouseout a pie slice.
        function mouseout(d){
            // call the update function of histogram with all data.
            hG.update(fData.map(function(v){
                return [v.State,v.total];}), barColor);
        }
		*/

        // Animating the pie-slice requiring a custom function which specifies
        // how the intermediate paths should be drawn.
        function arcTween(a) {
            var i = d3.interpolate(this._current, a);
            this._current = i(0);
            return function(t) { return arc(i(t));    };
        }    

        pieExists = true;
        return pC;
    }
    
    // function to handle legend.
    function legend(lD){
        var leg = {};
            
        // create table for legend.
        var legend = d3.select(id).append("table").attr('class','legend');
        
        // create one row per segment.
        var tr = legend.append("tbody").selectAll("tr").data(lD).enter().append("tr");
            
        // create the first column for each segment.
        tr.append("td").append("svg").attr("width", '16').attr("height", '16').append("rect")
            .attr("width", '16').attr("height", '16')
			.attr("fill",function(d){ return matchColor(d.type); });
            
        // create the second column for each segment.
        tr.append("td").text(function(d){ return d.type;});

        // create the third column for each segment.
        tr.append("td").attr("class",'legendFreq')
            .text(function(d){ return d3.format(",")(d.total);});

        // create the fourth column for each segment.
        tr.append("td").attr("class",'legendPerc')
            .text(function(d){ return getLegend(d,lD);});

        // Utility function to be used to update the legend.
        leg.update = function(nD){
            // update the data attached to the row elements.
            var l = legend.select("tbody").selectAll("tr").data(nD);

            // update the frequencies.
            l.select(".legendFreq").text(function(d){ return d3.format(",")(d.total);});

            // update the percentage column.
            l.select(".legendPerc").text(function(d){ return getLegend(d,nD);});        
        }
        
        function getLegend(d,aD){ // Utility function to compute percentage.
            return d3.format("%")(d.total/d3.sum(aD.map(function(v){ return v.total; })));
        }
        //alert(namesAndColors);
        legendExists = true;
        return leg;
    }
    
    /*
    // calculate total frequency by segment for all state.
    var tF = ['low','mid','high'].map(function(d){ 
        return {type:d, freq: d3.sum(fData.map(function(t){ return t.freq[d];}))}; 
    });    
    */

    var pF = fData.map(function(d){return [d.type,d.total];});
    
    // calculate total frequency by state for all segment.
    var sF = fData.map(function(d){return [d.partName,d.total];});

    if (histo) {
    	var hG = histoGram(sF); // create the histogram.
	}
	if (pie) {
    	var pC = pieChart(fData); // create the pie-chart.
    }
    if (legendBool) {
    	var leg= legend(fData);  // create the legend.
	}
}