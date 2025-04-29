package com.myoutdoor.agent.utils

import com.myoutdoor.agent.R
import java.util.*

internal object ExpandableListData {
   val data: HashMap<String, List<String>>
   get() {
      val expandableListDetail = HashMap<String, List<String>>()
      val expandableNestedListDetail = HashMap<String, List<String>>()
      val my_Account_array = arrayOf<String>(
         "How do I create an account?","How do I choose a password?",
         "How can i receive text messages/mobile notifications?","How do i update my information?",
         "How do i change my password?","I've forgotten my password")

      val property_array= arrayOf<String>("What is a Property Listing/RLU?","How can I find available properties?",
          "How do I filter my search?","Can I save my search?","How do I find my saved searches?",
          "How do I navigate the map?","Can my licence be terminated?","Can I bring guests on a property?",
          "What about insurance?","How soon can I access the property?","Are motorized vehicles allowed?",
          "What is a Request Pre-Approval for a licence?","What happens after I have been pre-approved for property/RLU?",
          "How do I add Members to a property?","What information am I required to enter for Invited Members?",
          "How do I remove an Invited Member from an active Property Listing Licence?",
          "How do I view my licence agreement?","How do I view my active properties?")


      val pay_online_array= arrayOf<String>("How do I pay my licence fee?","What types of payments are accepted?")

      val myAccount: MutableList<String> = ArrayList()
      myAccount.add(my_Account_array[0])
      myAccount.add(my_Account_array[1])
      myAccount.add(my_Account_array[2])
      myAccount.add(my_Account_array[3])
      myAccount.add(my_Account_array[4])
      myAccount.add(my_Account_array[5])

      val myAccount1: MutableList<String> = ArrayList()
      myAccount1.add(R.string.account_head_description1.toString())

      val support_array= arrayOf<String>("How do I contact a property agent?","Technical support")

      /* myAccount.add("How do I create an account?")
      myAccount.add("How do I choose a password?")
      myAccount.add("How can i receive text messages/mobile notifications?")
      myAccount.add("How do i update my information?")
      myAccount.add("How do i change my password?")
      myAccount.add("I've forgotten my password")*/

      val property: MutableList<String> = ArrayList()
      property.add(property_array[0])
      property.add(property_array[1])
      property.add(property_array[2])
      property.add(property_array[3])
      property.add(property_array[4])
      property.add(property_array[5])
      property.add(property_array[6])
      property.add(property_array[7])
      property.add(property_array[8])
      property.add(property_array[9])
      property.add(property_array[10])
      property.add(property_array[11])
      property.add(property_array[12])
      property.add(property_array[13])
      property.add(property_array[14])
      property.add(property_array[15])
      property.add(property_array[16])
      property.add(property_array[17])

       /* property.add("What is a Property Listing/RLU?")
      property.add("How can I find available properties?")
      property.add("How do I filter my search?")
      property.add("Can I save my search?")
      property.add("How do I find my saved searches?")
      property.add("How do I navigate the map?")
      property.add("Can my licence be terminated?")
      property.add("Can I bring guests on a property?")
      property.add("What about insurance?")
      property.add("How soon can I access the property?")
      property.add("Are motorized vehicles allowed?")
      property.add("What is a Request Pre-Approval for a licence?")
      property.add("What happens after I have been pre-approved for property/RLU?")
      property.add("How do I add Members to a property?")
      property.add("What information am I required to enter for Invited Members?")
      property.add("How do I remove an Invited Member from an active Property Listing Licence?")
      property.add("How do I view my licence agreement?")
      property.add("How do I view my active properties?")*/

      val payingOnline: MutableList<String> = ArrayList()
      payingOnline.add(pay_online_array[0])
      payingOnline.add(pay_online_array[1])

      /* payingOnline.add("How do I pay my licence fee?")
      payingOnline.add("What types of payments are accepted?")*/

      val support: MutableList<String> = ArrayList()
      support.add(support_array[0])
      support.add(support_array[1])

      /* support.add("How do I contact a property agent?")
      support.add("Technical support")*/


      expandableNestedListDetail[my_Account_array[0]]= myAccount1


      expandableListDetail["Account"] = myAccount
      expandableListDetail["Property"] = property
      expandableListDetail["Paying Online"] = payingOnline
      expandableListDetail["Support"] = support






      return expandableListDetail
   }
}