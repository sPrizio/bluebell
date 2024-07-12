import styles from "./layout.module.scss";
import SimpleHero from "@/app/components/Hero/SimpleHero/SimpleHero";
import StepSection from "@/app/components/Section/Step/StepSection";
import ContactSection from "@/app/components/Section/Contact/ContactSection";
import fees from '@/app/assets/images/content/step/image/fees.webp';
import plan from '@/app/assets/images/content/step/image/plan.webp';
import promise from '@/app/assets/images/content/step/image/promise.webp';
import testBanner from '@/app/assets/images/content/test-hero.jpg'
import pot from '@/app/assets/images/content/step/section/reuse.png'
import handPot from '@/app/assets/images/content/step/section/hand-with-growing-plant.png'
import growth from '@/app/assets/images/content/step/section/flowers.png'
import StepImageSection from "@/app/components/Section/Step/StepImageSection";
import ImageSection from "@/app/components/Section/Image/ImageSection";
import SimpleButton from "@/app/components/Button/SimpleButton";

/**
 * The home page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function HomePage() {

  const baseClass: string = "home-page"

  //  TODO: responsive design
  //  TODO: back to top button

  //  GENERAL FUNCTIONS

  function getImageSectionText() {
    return (
      <>
        <div>
          Work with our team of professionals to organize your entire financial profile. Got debt to consolidate? Not sure where you should be
          putting your money? Feeling like you&apos;re being left behind? We have you covered!
        </div>
        <br />
        <div>Our offerings include (but are not limited to):</div>
        <ul>
          <li>Budgeting</li>
          <li>Debt Consolidation</li>
          <li>Wealth Management</li>
          <li>Investing Strategy</li>
        </ul>
        <div>
          We also offer access to an award-winning<sup>[1]</sup>, proprietary investing platform and a host of other unique and personalized services
          to tackle your unique challenges. Let&apos;s get started!
        </div>
        <br/>
      </>
    )
  }

  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <p>MOBILE READINESS MARKER</p>
      <SimpleHero
        title={"Now you know a guy."}
        text={
          'Finances can be messy, money can be complicated and everyone seems to have the only advice you\'ll ever need to hear. We get it, we\'ve been there. ' +
          'Find out how simple things can be and empower your growth. Let your money work for you, so you can work on finding the next best ramen spot.'
        }
        variant={'image'}
        alignment={'center'}
        size={"large"}
        image={{
          src: testBanner,
          alt: 'Test Banner',
          height: 0,
          width: 0
        }}
        hasNavBar={true}
      />
      <ImageSection
        title={'Take Control of Your Money. Empower Your Future.'}
        text={getImageSectionText()}
        variant={'white'}
        alignment={'center'}
        size={"large"}
        image={{
          src: testBanner,
          alt: 'Test Banner',
          height: 0,
          width: 0
        }}
        cta={<SimpleButton text={'Our Services'} variant={"tertiary"} />}
      />
      <StepImageSection
        title={"Finances can be simple, and beautiful."}
        elements={[
          {
            title: 'Big Picture, Bigger Savings',
            text: 'Our deep, in-depth approach covers every facet of your financial picture. We build a plan tailored to your goals and what your definition of financially independent is.',
            image: {
              src: promise,
              alt: 'Promise imaging'
            }
          },
          {
            title: 'Planning Geared to You',
            text: 'We build a challenging, yet realistic financial plan that covers what you want, in plain and simple language. No gimmicks, no fine print, no confusion.',
            image: {
              src: plan,
              alt: 'Plan imaging'
            }
          },
          {
            title: 'Plain and Simple Fees',
            text: 'What you see is what you pay. No hidden fees, no hidden charges. You will always know exactly what you\'ll be paying and to whom it will be payed.',
            image: {
              src: fees,
              alt: 'Fees imaging'
            }
          }
        ]}
        hasBackground={true}
      />
      {/*<ClientCardSection
        title={'Your Goals, Our Mission'}
        subtitle={'No matter your stage in life & career, we\'re ready to serve you and your needs. Our goal is to support your pursuit of financial independence and to help you achieve your goals'}
        elements={[
          {
            title: 'Business Owners',
            text: 'I want to help my business continue to thrive, while also planning for retirement.',
            image: {
              src: boe,
              alt: 'Business Executives'
            }
          },
          {
            title: 'Families',
            text: 'I want to leverage the wealth I\'ve acquired to support my dependents and grow as a unit.',
            image: {
              src: pr,
              alt: 'Pre-Retirees'
            }
          },
          {
            title: 'Retirees',
            text: 'I am retired and want to protect my estate, ensure my succession and not run out of money in the process.',
            image: {
              src: r,
              alt: 'Retirees'
            }
          },
          {
            title: 'Private Clients',
            text: 'I possess a unique portfolio and require sophisticated solutions to complex problems.',
            image: {
              src: pc,
              alt: 'Private Clients'
            }
          },
          {
            title: 'Young Professionals',
            text: 'I\'m young and seeking to grow my assets, eliminate debt and set myself up for the future.',
            image: {
              src: yp,
              alt: 'Young Professionals'
            }
          }
        ]}
      />*/}

      <StepSection
        title={'A Journey of Growth, Together'}
        elements={[
          {
            title: 'Plant',
            text: 'We take a good, hard look at everything you own and owe. We start developing the outline and plan to set up your path to success. You\'ll be surprised at how well you\'re doing and close you actually are to your goals.',
            image: {
              src: handPot['src'],
              alt: 'default_image_1',
            }
          },
          {
            title: 'Nourish',
            text: 'Collaborate with a dedicated advisor to establish your growth strategy. Budget spending, Allocate income to invest, Consolidate debt. Build your future one step at a time. We\'ll be there every step of the way.',
            image: {
              src: pot['src'],
              alt: 'default_image_3',
            }
          },
          {
            title: 'Grow',
            text: 'Reap the benefits of your diligent planning. Watch your wealth grow with a few simple habits. Leverage your new skills to further develop your financial profile. Enjoy your success!',
            image: {
              src: growth['src'],
              alt: 'default_image_2',
            }
          },
        ]}
        cta={<SimpleButton text={"Get Started"} variant={"tertiary"} />}
      />
      {/*<ColumnHero
        title={'Our Firm at a Glance'}
        elements={[
          {
            title: '$25.86B',
            text: 'Assets under management'
          },
          {
            title: '2024',
            text: 'year founded'
          },
          {
            title: '12+',
            text: 'professionals'
          }
        ]}
        disclaimer={'*Reflects combined AUM as of 3.31.2024 for bluebell and its subsidiaries.'}
        variant={"secondary"}
        size={"small"}
      />*/}
      <ContactSection/>
    </div>
  );
}
