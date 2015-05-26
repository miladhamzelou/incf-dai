<h1>How to Manage Resources</h1>

It is important for publishers and administrators to not only publish resources to the incfportal, but to manage their published resources as well. Tasks such as updating existing records with new information, setting the access level of a resource, reviewing and approving records (if you are an administrator), and maintaining updated registration information for registered resources on the network are important maintenance tasks for resources within the incfportal. This topic discusses how to use the <b>Manage</b> page on the incfportal web application, and then discusses different ways to edit resources.

<p>Resources are managed through the <b>Administration > Manage</b> interface in the incfportal web application. A publisher user can only view and manage the resources he/she owns, whereas an administrator can view and manage all the records uploaded by all publishers and him/herself. The <b>Manage Resources</b> page, available when a publisher or administrator clicks <b>Administration > Manage</b>, has five parts. Each is described below.<br>
</p>
<ul><li><b>Links at the Top</b>: At the top of the page, just above the Manage Resources text, there are two links.<br>
<ul><li> <b>Manage</b>: brings users to the Manage Resources page, and is activated by default when a user first clicks the Administration tab.<br>
</li><li> <b>Add</b>: directs users to a list of choices for adding a resource to the incfportal through publishing.<br>
</li></ul>
</li><li><b>Manage resources search fields</b>: The Manage Resources search allows publishers to search through the resources they published to the incfportal. Underneath the Manage Resources text, there are fields that help organize the list of resources. By filling these fields with terms, publishers can filter the records he/she owns to find specific records. Each field is described below. The filters are applied when the publisher clicks <b>Search</b>.<br>
</li></ul>
<table>

<tr>
<th> Filter<br>
</th><th> Description<br>
</th></tr>
<tr>
<td> Document title<br>
</td><td> searches the resources' title elements for the search term<br>
</td></tr>
<tr>
<td> Document UUID<br>
</td><td> searches for the DocUID from the incfportal database (will be the same DocUID in the resource's REST URL)<br>
</td></tr>
<tr>
<td> Document owner<br>
</td><td> Select a publisher from this drop down list to search for resources that are owned by that publisher. If you are not a incfportal administrator, this drop down list will only populate with your username.<br>
</td></tr>
<tr>
<td> Approval status<br>
</td><td> Select a status from this drop down list to search for resources that have a status of Posted, Approved, Incomplete, Disapproved, Reviewed, or Draft. If the Any option is selected, then resources will be returned regardless of their status.<br>
</td></tr>
<tr>
<td> Publication method<br>
</td><td> Select a publication method from this drop down list to search for resources that were published to the incfportal by a certain method:<br>
<ul><li> Any: resources will be returned regardless of their publication method<br>
</li><li> Registration: resources registered as a network resource<br>
</li><li> Upload: resources published by Uploading<br>
</li><li> Editor: resources published through the incfportal's Create Metadata form<br>
</li><li> Synchronization: resources published from synchronizing with a registered network resource<br>
</li><li> Batch: resources published using the incfportal Publish Client<br>
</li><li> Other: resources published by a means other than Registration, Upload, Editor, Synchronization, or Batch<br>
</li></ul>
</td></tr>
<tr>
<td> Update date<br>
</td><td> These two fields define the beginning and ending dates between which the resource was most recently updated<br>
</td></tr></table>
<p>When results are returned, you can work with each resource individually, or if you are an Administrative user you can apply changes to the status of all resulting records by checking the <b>Apply action to the entire result set</b> checkbox. Note, this checkbox will only appear after you have carried out a search using the manage resources search fields, and any status changes you apply will be reflected in the incfportal Search page after the Lucene index synchronizes with the incfportal database (a process that occurs at a scheduled time increment set in the incfportal configuration files).<br>
</p>
<ul><li><b>For selected records drop down list</b>: Below the search fields section, there is a drop down box where publishers can dictate specific actions that change the status of selected resources in the resources table. The actions available from the drop down list are different for publishers and administrators. Actions are carried out when a user checks the checkbox next to one or more resources in the resources table, chooses an action, and clicks the <b>Execute Actio</b>n button. The table below describes the actions and which user roles can access them.<br>
</li></ul>
<table>

<tr>
<th> Action<br>
</th><th> Description<br>
</th><th> Roles that can access<br>
</th></tr>
<tr>
<td> Set as Posted<br>
</td><td> Posted resources have not yet been approved, and therefore cannot be discovered through the incfportal search interface.<br>
</td><td> publisher, administrator<br>
</td></tr>
<tr>
<td> Set as Approved<br>
</td><td> Marks the resource as searchable through the incfportal interface.<br>
</td><td> administrator<br>
</td></tr>
<tr>
<td> Set as Incomplete<br>
</td><td> Marks the resource as not having sufficient information to be approved.<br>
</td><td> publisher, administrator<br>
</td></tr>
<tr>
<td> Set as Disapproved<br>
</td><td> Marks the resource as being reviewed and not approved by the incfportal administrator. Disapproved resources are not discoverable through the incfportal search interface.<br>
</td><td> administrator<br>
</td></tr>
<tr>
<td> Set as Reviewed<br>
</td><td> Marks the resource as having been reviewed by the incfportal administrator, and discoverable in search results.<br>
</td><td> administrator<br>
</td></tr>
<tr>
<td> Transfer Ownership<br>
</td><td> Allows the administrator to select the resource's new owner from a drop down list that appears below the actions drop down box. Changing the ownership of a resource will not change its status or its update date.<br>
</td><td> administrator<br>
</td></tr>
<tr>
<td> Delete<br>
</td><td> Deletes the resource from the incfportal.<br>
</td><td> publisher, administrator<br>
</td></tr>
<tr>
<td> Set Access Level<br>
</td><td> Allows the publisher to determine what groups should be allowed access the resource. For more information, see <a href='/apps/mediawiki/incfportal/index.php?title=How_to_Restrict_Access_to_Resources' title='How to Restrict Access to Resources'>How to Restrict Access to Resources</a>
</td><td> publisher, administrator<br>
</td></tr></table>
<ul><li><b>Resources Table</b>: This table lists all of the resources returned as a result of the Manage Resources search field criteria. Above the resources table is text indicating how many records were returned and how many are displayed on the page. If many resources are returned, results may span more than one page, and can be navigated using the page number boxes just above the table. There are several headings in the table. The resources in the table can be sorted according to the headings by clicking on the heading. Each record has an empty checkbox and also buttons next to it. Select the record by clicking the checkbox, and then choose an action to be executed from the For selected records drop down list above the table.<br>
</li><li><b>Icons next to each resource in the table</b>: There are two types of resources in the resource table. One type is metadata documents that are published by uploading, using the Publish Client, synchronized from a network resource, or created using the incfportal editor. The other type is registered resources on the network. In the resources table, each resource button icons next to it. Some buttons appear for both types of resources, whereas others appear only for one or the other. The table below describes the buttons, their functionalities, and with which types of resources they are used.<br>
</li></ul>
<table>

<tr>
<th> Icon name<br>
</th><th> Description<br>
</th><th> Type of resource<br>
</th></tr>
<tr>
<td> View </td><td> Opens a new window displaying the raw metadata XML for the resource.<br>
</td><td> metadata resource<br>
</td></tr>
<tr>
<td> History<br>
</td><td> Navigates to the <b>Synchronization History</b> page, where it is possible to view reports on the synchronization sessions.<br>
</td><td> registered resource on the network<br>
</td></tr>
<tr>
<td> Edit<br>
</td><td> If the resource was published using an online form in the incfportal interface, then this icon will be activated. It opens the form and allows users to change the resource's metadata (for metadata resources) or registration information (for registered resources on the network). This button will be grayed out if the record was published through Upload, the Publish Client, or synchronization from another resource.<br>
</td><td> metadata resource and registered resource on the network<br>
</td></tr>
<tr>
<td> Download<br>
</td><td> Allows the owner of the resource to download the metadata resource from the incfportal. The downloaded XML document can be imported in ArcCatalog with the original dataset or updated using external XML editors.<br>
</td><td> metadata resource<br>
</td></tr>
<tr>
<td> Synchronize content </td><td> Initiates a synchronization session immediately upon clicking. Once clicked, the user will be prompted to continue and synchronization will begin.<br>
</td><td> registered resource on the network<br>
</td></tr>
<tr>
<td> Cancel Synchronization<br>
</td><td> If the resource is being synchronized at this moment, this icon will replace the Synchronize content icon. Clicking the Cancel synchronization icon will prompt the user if he/she wants to cancel the synchronization process. If he/she chooses to cancel it, all records that have already been synchronized during that session will remain in the incfportal, but the synchronization session will immediately halt.<br>
</td><td> registered resource on the network<br>
</td></tr>
<tr>
<td> Delete<br>
</td><td> Allows the owner of the resource to delete the selected resource from the collection.<br>
</td><td> metadata resource and registered resource on the network<br>
</td></tr>
<tr>
<td> Find Parent Resource </td><td> Will find and display the parent resource from which the selected resource is synchronized.<br>
</td><td> metadata resource<br>
</td></tr>
<tr>
<td> Find Child Resources </td><td> Will find and display all resources synchronized from the selected registered network resource.<br>
</td><td> registered resource on the network<br>
</td></tr></table>
<a></a><h2> <span>Editing Resources</span></h2>
<p>There are a number of ways to edit resources published to the incfportal. Best practice is to edit metadata at its source, and then republish the metadata so it is updated in the incfportal catalog. For example, if you use ArcGIS Desktop to author and update metadata, you would want to make your edits to the metadata using ArcCatalog, and then republish by either registering your resource on the network, uploading the updated metadata xml, or running the Publish Client. This way, the latest information is published to the incfportal, but the information is also associated with your data resource as well.<br>
</p><p>Sometimes it is not possible to edit metadata at the source. In these cases, it is possible to use tools provided by the incfportal to edit the information about the resource. For example, if you used the incfportal's dedicated editor to create metadata manually, then you can use the dedicated editor to edit and update the metadata. Or if you uploaded the metadata or it was synchronized from a registered resource on the network, you can edit the metadata using an external metadata editor. These two methods are described below.<br>
</p>
<a></a><h3> <span>Edit metadata using the incfportal editor</span></h3>
<p>The incfportal metadata editor is accessed from the <b>Administration</b> tab, when a publisher or administrator clicks the pencil icon next to a record in the document table. The metadata editor form is loaded and populated with the information previously entered describing the resource. After updating information in the form, click the <b>Save</b> button at the bottom of the screen. The old information will be replaced with your new information, and the resource is republished.<br>
</p>