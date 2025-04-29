package com.myoutdoor.agent.fragment

import android.util.Log
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.adapter.CustomExpandableListAdapter
import com.myoutdoor.agent.adapter.ThreeLevelListAdapter
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.ExpandableListData.data
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.toolbar.*

class FrequentlyAskedQuestionsFragment : BaseFragment() {

    var expandableListView: ExpandableListView? = null


    var parent = arrayOf("My Account","Property Listing","Paying Online","Support") // comment this when uncomment bottom
    var MyAccount = arrayOf("How do I create an account?", "How do I choose a password?","How can i receive text messages/mobile notifications?","How do i update my information?",
        "How do i change my password?","I've forgotten my password")

    var createAccount = arrayOf("At the top of the website in the right-hand corner, you will see a green button that says “LOGIN/REGISTER”."+" Click this button.You will now be directed to the login page. There are three choices to create an account.\n"+
            "1.Click the Facebook icon and sign in by connecting your Facebook account.\n"+
            "2.Click the Google+ icon and sign in by connecting your Google+ account.\n"+"" +
            "3.Click “Register”,fill out the form on the page, agree to the Terms of Service and click “Register”\n\n"+
            "You will receive a verification email after signing up. Your account will NOT be active until"+
            "you have confirmed your email address by clicking the verification link sent."+"Can´t find the email? Make sure to check you spam/junk folder from help@myoutdooragent.com.")

    var choosePassword = arrayOf("All account passwords must be eight or more characters in length. Following are some tips to help you create a strong password:\n\n"+
            "1. Create a password with at least eight characters.Short passwords are easier to guess.\n"+
            "2. Combine letters, numbers and symbols.\n"+
            "3. Create a password that is easy for you to remember,but difficult for others to guess.Do not use your log-in name, your spouse’s name or your birthday.\n"+
            "4. Do not use a password that is hard to remember or must be written down in order to remember.")
    var mobileNotification = arrayOf(
        "After creating and verifying your account, log in and click “My Account” in the top right-hand corner."+
                "From the drop-down menu, select “Profile”. On the Account Settings section, beside “Enable Mobile Notifications”,you will need to slide the toggle to the right side."+
                "A green check mark will appear. You have now enabled mobile notifications.\n\n"+
                "**Note: You need to update your account setting information to include a mobile number before enabling mobile notifications.")

    var myinformation= arrayOf("1. Log into your account\n" +
            "2. Click the dropdown menu under “My Account”\n" +
            "3. Select “Profile”\n" +
            "4. Update your information in the boxes provided.")

    var mypassword= arrayOf(" 1. Log into your account\n" +
            " 2. Click the dropdown menu under “My Account”\n" +
            " 3. Select “Profile”\n" +
            " 4. In the bottom, right-hand corner, click the red button that says “RESET PASSWORD”\n" +
            " 5. You will receive an email including a link to reset your password.\n")


    var forgottenpassword= arrayOf("Have you forgotten your password? No problem. When logging into your account, select “Forgot My Password” in the right-hand corner of the login box. A link will be sent to your email for you to reset your password.")




    var PropertyLisiting = arrayOf("What is a Property Listing/RLU?","How can I find available properties?",
        "How do I filter my search?","Can I save my search?","How do I find my saved searches?",
        "How do I navigate the map?","Can my licence be terminated?","Can I bring guests on a property?",
        "What about insurance?","How soon can I access the property?","Are motorized vehicles allowed?",
        "What is a Request Pre-Approval for a licence?","What happens after I have been pre-approved for property/RLU?",
        "How do I add Members to a property?","What information am I required to enter for Invited Members?",
        "How do I remove an Invited Member from an active Property Listing Licence?",
        "How do I view my licence agreement?","How do I view my active properties?")


    var RLU =
        arrayOf("A Property Listing/Recreational License Unit (RLU) is the term used to identify an individual tract of land that may be licensed by an individual and/or a hunt club for recreational use, such as hunting. The RLU is defined by number of acres, boundaries,\n" +
                "license length and other characteristics specific to the tract of land. An individual or hunt club may license more than one RLU.")

    var availableProperties = arrayOf("In the top header, click “SEARCH”. This will direct you to the available Property Listings and dynamic map. You can scroll through the available list of properties on the left-hand side,\n" +
            "interact with the dynamic map, or filter your search by clicking “Show Filters”, select your desired filters and click “Apply”.")

    var mySearch = arrayOf(
        "After navigating to the available Property Listings by clicking “SEARCH” in the top header, you can click filters in the top left-hand corner.A box will appear on the left side of the page with a number of filters for you to select from. After selecting the appropriate filters, click “Apply”.\n" + "The property listings will then be filtered down to the respective search."

    )
    var saveMySearch = arrayOf("Yes.After selecting the desired filters and clicking “Apply”, You can click the green icon on the right- hand search. This will save your filter to your Account.")


    var findSavedSearch = arrayOf("Start by logging into your account. Then, click “MY ACCOUNT” in the top, right-hand corner of the page. From the drop-down menu, select “Profile”. On the left-hand side, you will see two tabs. Select “Saved Searches”.\n" +
            "On this page, you can view your saved searches or delete previous saved searches you no longer wish to save.")

    var navigateMap = arrayOf("1. Click and drag the map to explore available Property Listings/Recreational License Units (RLUs).\n"+
            "2. Click on directional arrows to move the map. Click + to zoom in and – to zoom out.\n" +
            "3. Mouse over and click a marker to view Property Listing details and to License Now or Request Pre-Approval.\n" +
            "4. Use the Filters on the left-hand side to help you find the property you want.\n" +
            "Note: Property Listing boundaries will be displayed on the map when you zoom in.")


    var licenseTerminated = arrayOf("Generally, at any time during the term, Agent shall have the right upon notice to Member to immediately revoke and terminate the license and the rights and privileges of licensees. For specific terms, see the license agreement.")
    var guestsproperty = arrayOf("In order to bring additional guests or members onto the Property Listing/RLU,"+
            "you will need to add each individual as an Invited Member to that specific property listing."+"That Invited Member will need to register their own My Outdoor Agent account and accept the license terms the specific Property Listing.")

    var Insurance = arrayOf("You will need to review the specific details of your property listing for the exact insurance provided." )

    var accessProperty = arrayOf("You may not access the property listing until you have received a notification that your agreement has been legally executed and your license period begins.")

    var vehiclesAllowed = arrayOf("Please refer to your license agreement for information regarding motorized vehicle use on your Property Listing/RLU.")

    var preApproval = arrayOf("Each Agent has the opportunity to review your application before confirming acceptance for licensing the property.")

    var PreApprovalForRLU = arrayOf("Once you have been pre-approved for a property, you will receive a notification by email. This does NOT mean your license has been executed. To execute your license, you will need too:\n\n"+
            "1. Log in to your account\n" +
            " 2. Click the drop-down menu under “My Account”\n" +
            "3. Select “Pre-Approval”\n" +
            "4. On the RLU/property which has been accepted, click “View Details”\n" +
            "This will direct you back to the property listing\n" +
            " 5. Click License Now\n" +
            " 6. This will automatically bring your license agreement up – review the license agreement in detail.\n" +
            " 7. After you have reviewed, click “ACCEPT AND BUY NOW”\n" +
            " 8. This will direct you to our secure payment gateway.\n" +
            "NOTICE: We ONLY accept online payments.\n" +
            " 9. Once your payment has been approved, the license will be active, and a copy of your license agreement will be stored within your account.\n\n"+"You will receive a verification email after signing up. Your account will NOT be active until you have confirmed your email address by clicking the verification link sent. Can´t find the email? Make sure to check you spam/junk folder from help@myoutdooragent.com.")


    var membersProperty = arrayOf("After you have executed your license agreement (signed and payed the fee),\n" +
            " you will be immediately directed to the license details.\n" +
            "On the top right side of the page, you will see a green button that says, “INVITE MEMBERS”.\n" +
            " After clicking this button, a pop-up box will appear. To invite new members,\n" +
            " type or paste their emails below in the available text area that says, “Enter email ID…”")

    var invitedMembers = arrayOf(" You will need to provide an email address for your Invited Members when you invite them to an active Property Listing/RLU.\n" +
            "Your members will be responsible for creating their own account and entering in their own details and accepting the terms of the license agreement.")
    var propertyListingLicence = arrayOf("Invited Members may be deleted from the Property Listing License at any time. The owner of a Property Listing is the only person who can delete members. If you are a Property Listing owner, you can:\n\n" +
            "1. Log into your account\n" +
            "2. Click the dropdown menu under “My Account”\n" +
            "3. Select “Property License”\n" +
            "4. For the active property license, you would like to remove a member from, click the green button that says “View Details” under the respective property license.\n" +
            "5. In the top left-hand corner, you will see a list of your current members.\n" +
            "6. Select the member you would like to remove and click the red trashcan on the right-hand side of their name under the column “Action”.")
    var licenceAgreement = arrayOf("After you have reviewed, accepted the terms and paid your license fees, you will immediately be directed to the license details page. You can click the green button on the top of the page that says “VIEW PROPERTY LICENSE AGREEMENT” to download a copy of your license.\n" +
            "Should you want to review your agreement at a later date, you can access this agreement at any time by:\n\n" +
            "1. Logging into your account\n" +
            "2. Click the dropdown menu under “My Account”\n" +
            "3. Click “Property License”\n" +
            "4. Select the license you would like to view under “My Active Property Licenses” by clicking the green button “View Details”\n" +
            "5. Click the green button on the top of the page that says “VIEW PROPERTY LICENSE AGREEMENT”\n" +
            "6. Read, download or print the Property Listing agreement")
    var activeProperties = arrayOf("After logging into “my account”, you will be directed to your account homepage.\n" +
            "From there, click on the drop-down menu and click “Licenses”.\n" +
            "Active licenses are shown in their respective classes.\n" +
            "For example, if you are the owner of a license, this license will be displayed under the box, “My Licenses”.\n" +
            "If you are a member of another property, this will be displayed under “My Memberships”.")





    var PayingOnline= arrayOf("How do I pay my licence fee?","What types of payments are accepted?")
    var payLicenceFees= arrayOf("After you have been preapproved for a property listing, you will receive an email with further instructions. You will then need to:\n\n" +
            "1. Log back into your account\n" +
            "2. Click the drop-down menu under “My Account”\n" +
            "3. Click “Pre-Approval”\n" +
            "4. For the property listings that have been approved, there will be a green box that says “License Now”\n" +
            "5. Click “View Details” (this will direct your back to the property details page)\n" +
            "6. Click the green button that says “License Now”\n" +
            "7. Review the license agreement and scroll to the button\n" +
            "8. Click “ACCEPT AND BUY NOW”\n" +
            "9. This will direct you to our online payment platform")


    var paymentAccepted= arrayOf(" We accept credit card and ACH payment methods.")

    var Support= arrayOf("How do I contact a property agent?","Technical support")
    var propertyAgent= arrayOf("To contact a property agent, you first need to select the property for which you have a question regarding."+
            "If this not an active license, navigate to the property details page. Scroll down. At the bottom of the page in the right-hand corner,"+
            "you will see a box that says Property Listing Contact information.Note: not all Agents will list the same contact information.\n\n" +
            "If you have already sent a message to a property agent previously, you can (after logging in) navigate to your message center by clicking “Messages” on the top header." +
            "This will direct you to your message center and display all previous messages with your property agent.\n\n"+
            " If you have a question for an ACTIVE property license, you have can:\n\n" +
            "1. Log in to your account\n" +
            "2. Click “Messages” in the top header\n" +
            "3. Any Property Listing you have an active Property Listing License for will automatically show up in your message center.\n" +
            "You can contact the Property Listing Agent directly here.\n\n"+
            "You will receive a verification email after signing up. Your account will NOT be active until you have confirmed your email address by clicking the verification link sent. Can´t find the email? Make sure to check you spam/junk folder from help@myoutdooragent.com.")


    var technicalSupport= arrayOf("Do you have technical questions regarding using the site? Please fill out our contact us form with your question. One of our team members should be back in touch with you within three (3) to five (5) business days")

    var thirdLevelMyAccount = LinkedHashMap<String, Array<String>>()

    var thirdLevelPropertyListings = LinkedHashMap<String, Array<String>>()
    var thirdLevelPayingOnline = LinkedHashMap<String, Array<String>>()
    var thirdLevelSupport = LinkedHashMap<String, Array<String>>()


    var secondLevel: MutableList<Array<String>>? = ArrayList()
    var data: MutableList<LinkedHashMap<String, Array<String>>> = ArrayList()





    override fun getLayoutId(): Int {
        return R.layout.fragment_frequently_asked_questions
    }

    override fun onCreateView() {
        tvToolbar.setText("Help")
//      backpress button
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility= View.VISIBLE
        }


        val dataa= requireActivity().resources.getString(R.string.account_head_description1)
        Log.e("@@@@@@","String::"+dataa)
        // second level category names (genres)
        secondLevel!!.add(MyAccount)
        secondLevel!!.add(PropertyLisiting)
        secondLevel!!.add(PayingOnline)
        secondLevel!!.add(Support)
        // secondLevel.add(serials);

        // movies category all data
        thirdLevelMyAccount[MyAccount[0]] = createAccount
        thirdLevelMyAccount[MyAccount[1]] = choosePassword
        thirdLevelMyAccount[MyAccount[2]] = mobileNotification
        thirdLevelMyAccount[MyAccount[3]]=  myinformation
        thirdLevelMyAccount[MyAccount[4]]=  mypassword
        thirdLevelMyAccount[MyAccount[5]]=  forgottenpassword



        // games category all data
        thirdLevelPropertyListings[PropertyLisiting[0]] = RLU
        thirdLevelPropertyListings[PropertyLisiting[1]] = availableProperties
        thirdLevelPropertyListings[PropertyLisiting[2]] = mySearch
        thirdLevelPropertyListings[PropertyLisiting[3]] = saveMySearch
        thirdLevelPropertyListings[PropertyLisiting[4]] = findSavedSearch
        thirdLevelPropertyListings[PropertyLisiting[5]] = navigateMap
        thirdLevelPropertyListings[PropertyLisiting[6]] = licenseTerminated
        thirdLevelPropertyListings[PropertyLisiting[7]] = guestsproperty
        thirdLevelPropertyListings[PropertyLisiting[8]] = Insurance
        thirdLevelPropertyListings[PropertyLisiting[9]] = accessProperty
        thirdLevelPropertyListings[PropertyLisiting[10]] = vehiclesAllowed
        thirdLevelPropertyListings[PropertyLisiting[11]] = preApproval
        thirdLevelPropertyListings[PropertyLisiting[12]] = PreApprovalForRLU
        thirdLevelPropertyListings[PropertyLisiting[13]] = membersProperty
        thirdLevelPropertyListings[PropertyLisiting[14]] = invitedMembers
        thirdLevelPropertyListings[PropertyLisiting[15]] = propertyListingLicence
        thirdLevelPropertyListings[PropertyLisiting[16]] = licenceAgreement
        thirdLevelPropertyListings[PropertyLisiting[17]] = activeProperties



        thirdLevelPayingOnline[PayingOnline[0]]=payLicenceFees
        thirdLevelPayingOnline[PayingOnline[1]]=paymentAccepted

        thirdLevelSupport[Support[0]]=propertyAgent
        thirdLevelSupport[Support[1]]=technicalSupport


        // serials category all data
        /*  thirdLevelSerials.put(serials[0], crime);
        thirdLevelSerials.put(serials[1], family);
        thirdLevelSerials.put(serials[2], comedy);
*/

        // all data
        data.add(thirdLevelMyAccount)
        data.add(thirdLevelPropertyListings)
        data.add(thirdLevelPayingOnline)
        data.add(thirdLevelSupport)
        //data.add(thirdLevelSerials);

        // expandable listview
        expandableListView = requireView()!!.findViewById<View>(R.id.expandible_listview) as ExpandableListView

        // parent adapter
        val threeLevelListAdapterAdapter = ThreeLevelListAdapter(requireContext(), parent, secondLevel, data)

        // set adapter
        expandableListView!!.setAdapter(threeLevelListAdapterAdapter)


        // OPTIONAL : Show one list at a time
        expandableListView!!.setOnGroupExpandListener(object :
            ExpandableListView.OnGroupExpandListener {
            var previousGroup = -1
            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup) expandableListView!!.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })

        fragmentBackPressHandle()
    }

}