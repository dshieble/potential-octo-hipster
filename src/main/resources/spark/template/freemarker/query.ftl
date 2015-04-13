<#assign content>

	<div id="wrapper">
		<div id="query">
				<div class="target">
				<textarea id="suggest3" name="target1" rows="1" cols="50" placeholder="To Street 1"></textarea>
				<select class="suggestions" id="list3"></select>
							
				<textarea id="suggest4" name="target2" rows="1" cols="50" placeholder="To Street 2"></textarea>
				<select class="suggestions" id="list4"> </select>
			</div>
			<div id="centerpiece">
				<h2>Ye High-Tech, Olde Providence</h1>
				<button id="get_path"type="button">Get Path</button>
			</div>
			
			<div class="source">
				<textarea id="suggest" name="source1" rows="1" cols="50" placeholder="From Street 1"></textarea>
				<select class="suggestions" id="list" rows="1" cols="50"></select>
			
				<textarea id="suggest2" name="source2" rows="1" cols="50" placeholder="From Street 2"></textarea>
				<select class="suggestions" id="list2"></select>
			</div>

		</div>
		<div class="map">
			<canvas id="map"></canvas>
			<img id = "compass" src="http://www.clipartbest.com/cliparts/dT8/57M/dT857MjTe.png" alt="a compass">
			
		</div>
		<button id="clear" type="button">Clear</button>
	</div>
</#assign>
<#include "main.ftl">
